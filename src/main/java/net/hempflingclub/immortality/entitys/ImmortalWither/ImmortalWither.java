package net.hempflingclub.immortality.entitys.ImmortalWither;

import com.google.common.collect.ImmutableList;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityAdvancementGiver;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ImmortalWither extends HostileEntity implements SkinOverlayOwner, RangedAttackMob {
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = LivingEntity::isPlayer;
    private final ServerBossBar bossBar;
    private final int[] skullCooldowns = new int[2];
    private final int[] chargedSkullCooldowns = new int[2];
    private int blockBreakingCooldown;
    private static final TargetPredicate HEAD_TARGET_PREDICATE;
    private final float[] sideHeadPitches = new float[2];
    private final float[] sideHeadYaws = new float[2];
    private final float[] prevSideHeadPitches = new float[2];
    private final float[] prevSideHeadYaws = new float[2];
    private static final TrackedData<Integer> INVUL_TIMER;
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_1;
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_2;
    private static final TrackedData<Integer> TRACKED_ENTITY_ID_3;
    private static final List<TrackedData<Integer>> TRACKED_ENTITY_IDS;

    static {
        TRACKED_ENTITY_ID_1 = DataTracker.registerData(ImmortalWither.class, TrackedDataHandlerRegistry.INTEGER);
        TRACKED_ENTITY_ID_2 = DataTracker.registerData(ImmortalWither.class, TrackedDataHandlerRegistry.INTEGER);
        TRACKED_ENTITY_ID_3 = DataTracker.registerData(ImmortalWither.class, TrackedDataHandlerRegistry.INTEGER);
        TRACKED_ENTITY_IDS = ImmutableList.of(TRACKED_ENTITY_ID_1, TRACKED_ENTITY_ID_2, TRACKED_ENTITY_ID_3);
        INVUL_TIMER = DataTracker.registerData(ImmortalWither.class, TrackedDataHandlerRegistry.INTEGER);
        HEAD_TARGET_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(50.0).setPredicate(CAN_ATTACK_PREDICATE);
    }

    public ImmortalWither(EntityType<? extends ImmortalWither> entityType, World world) {
        super(entityType, world);
        this.bossBar = (ServerBossBar) (new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true).setThickenFog(true);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setHealth(this.getMaxHealth());
        this.experiencePoints = 5000;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_1, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_2, 0);
        this.dataTracker.startTracking(TRACKED_ENTITY_ID_3, 0);
        this.dataTracker.startTracking(INVUL_TIMER, 0);
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        if (source.getSource() != null && source.getSource().isPlayer()) {
            PlayerEntity killingPlayer = (PlayerEntity) source.getSource();
            ItemEntity itemEntity = this.dropItem(ImmortalityItems.HeartOfImmortality);
            if (itemEntity != null) {
                itemEntity.setCovetedItem();
            }
            ImmortalityAdvancementGiver.giveImmortalWitherSlayer(killingPlayer);
        }
    }

    @Override
    protected void initGoals() {
        this.targetSelector.add(1, new RevengeGoal(this));
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.5, 20, 50.0f));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 0, false, false, CAN_ATTACK_PREDICATE));
    }

    @Override
    public boolean shouldRenderOverlay() {
        return true;
    }

    @Override
    protected void mobTick() {
        int i;
        if (this.getInvulnerableTimer() > 0) {
            i = this.getInvulnerableTimer() - 1;
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            if (i <= 0) {
                Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
                this.world.createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, destructionType);
                if (!this.isSilent()) {
                    this.world.syncGlobalEvent(1023, this.getBlockPos(), 0);
                }
            }
            this.setInvulTimer(i);
            if (this.age % 10 == 0) {
                int immortalWitherDeaths = ImmortalityStatus.getImmortalWitherDeaths(this);
                if (this.getHealth() < this.getMaxHealth() * (1 - ((1.0F * immortalWitherDeaths) / 5))) {
                    this.heal(20.0F);
                } else {
                    //Cap Health to Phase Health Limit
                    this.setHealth(this.getMaxHealth() * (1 - ((1.0F * immortalWitherDeaths) / 5)));
                }

            }
        } else {
            super.mobTick();
            int j;
            for (i = 1; i < 3; ++i) {
                if (this.age >= this.skullCooldowns[i - 1]) {
                    this.skullCooldowns[i - 1] = this.age + 10 + this.random.nextInt(10);
                    if (this.getInvulnerableTimer() <= 0) {
                        int[] var10000 = this.chargedSkullCooldowns;
                        int var10001 = i - 1;
                        int var10003 = var10000[i - 1];
                        var10000[var10001] = var10000[i - 1] + 1;
                        if (var10003 > 15) {
                            double d = MathHelper.nextDouble(this.random, this.getX() - 10.0, this.getX() + 10.0);
                            double e = MathHelper.nextDouble(this.random, this.getY() - 5.0, this.getY() + 5.0);
                            double h = MathHelper.nextDouble(this.random, this.getZ() - 10.0, this.getZ() + 10.0);
                            this.shootSkullAt(i + 1, d, e, h, true);
                            this.chargedSkullCooldowns[i - 1] = 0;
                        }
                    }

                    j = this.getTrackedEntityId(i);
                    if (j > 0) {
                        LivingEntity livingEntity = (LivingEntity) this.world.getEntityById(j);
                        if (livingEntity != null && this.canTarget(livingEntity) && !(this.squaredDistanceTo(livingEntity) > 900.0) && this.canSee(livingEntity) && this.getInvulnerableTimer() <= 0) {
                            this.shootSkullAt(i + 1, livingEntity);
                            this.skullCooldowns[i - 1] = this.age + 40 + this.random.nextInt(20);
                            this.chargedSkullCooldowns[i - 1] = 0;
                        } else {
                            this.setTrackedEntityId(i, 0);
                        }
                    } else {
                        List<LivingEntity> list = this.world.getTargets(LivingEntity.class, HEAD_TARGET_PREDICATE, this, this.getBoundingBox().expand(20.0, 8.0, 20.0));
                        if (!list.isEmpty()) {
                            LivingEntity livingEntity2 = list.get(this.random.nextInt(list.size()));
                            this.setTrackedEntityId(i, livingEntity2.getId());
                        }
                    }
                }
            }

            if (this.getTarget() != null) {
                this.setTrackedEntityId(0, this.getTarget().getId());
            } else {
                this.setTrackedEntityId(0, 0);
            }
            if (this.blockBreakingCooldown > 0) {
                --this.blockBreakingCooldown;
                if (this.blockBreakingCooldown == 0 && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    i = MathHelper.floor(this.getY());
                    j = MathHelper.floor(this.getX());
                    int k = MathHelper.floor(this.getZ());
                    boolean bl = false;
                    for (int l = -1; l <= 1; ++l) {
                        for (int m = -1; m <= 1; ++m) {
                            for (int n = 0; n <= 3; ++n) {
                                int o = j + l;
                                int p = i + n;
                                int q = k + m;
                                BlockPos blockPos = new BlockPos(o, p, q);
                                BlockState blockState = this.world.getBlockState(blockPos);
                                if (canDestroy(blockState)) {
                                    bl = this.world.breakBlock(blockPos, true, this) || bl;
                                }
                            }
                        }
                    }
                    if (bl) {
                        this.world.syncWorldEvent(null, 1022, this.getBlockPos(), 0);
                    }
                }
            }
            if (this.age % 20 == 0) {
                int immortalWitherDeaths = ImmortalityStatus.getImmortalWitherDeaths(this);
                if (this.getHealth() < this.getMaxHealth() * (1 - ((1.0F * immortalWitherDeaths) / 5))) {
                    this.heal(5.0F);
                } else {
                    //Cap Health to Phase Health Limit
                    this.setHealth(this.getMaxHealth() * (1 - ((1.0F * immortalWitherDeaths) / 5)));
                }
            }
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source != DamageSource.DROWN && !(source.getAttacker() instanceof ImmortalWither)) {
            if (this.getInvulnerableTimer() > 0 && source != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                Entity entity;
                if (this.shouldRenderOverlay()) {
                    entity = source.getSource();
                    if (entity instanceof PersistentProjectileEntity) {
                        return false;
                    }
                }
                entity = source.getAttacker();
                if (!(entity instanceof PlayerEntity) && entity instanceof LivingEntity && ((LivingEntity) entity).getGroup() == this.getGroup()) {
                    return false;
                } else {
                    if (this.blockBreakingCooldown <= 0) {
                        this.blockBreakingCooldown = 20;
                    }
                    return super.damage(source, amount);
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void tickMovement() {
        Vec3d vec3d = this.getVelocity().multiply(1.0, 0.6, 1.0);
        if (!this.world.isClient && this.getTrackedEntityId(0) > 0) {
            Entity entity = this.world.getEntityById(this.getTrackedEntityId(0));
            if (entity != null && this.getInvulnerableTimer() <= 0) {
                double d = vec3d.y;
                if (this.getY() < entity.getY() || !this.shouldRenderOverlay() && this.getY() < entity.getY() + 5.0) {
                    d = Math.max(0.0, d);
                    d += 0.3 - d * 0.6000000238418579;
                }
                vec3d = new Vec3d(vec3d.x, d, vec3d.z);
                Vec3d vec3d2 = new Vec3d(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());
                if (vec3d2.horizontalLengthSquared() > 9.0) {
                    Vec3d vec3d3 = vec3d2.normalize();
                    vec3d = vec3d.add(vec3d3.x * 0.3 - vec3d.x * 0.6, 0.0, vec3d3.z * 0.3 - vec3d.z * 0.6);
                }
            }
        }

        this.setVelocity(vec3d);
        if (vec3d.horizontalLengthSquared() > 0.05) {
            this.setYaw((float) MathHelper.atan2(vec3d.z, vec3d.x) * 57.295776F - 90.0F);
        }
        super.tickMovement();
        int i;
        for (i = 0; i < 2; ++i) {
            this.prevSideHeadYaws[i] = this.sideHeadYaws[i];
            this.prevSideHeadPitches[i] = this.sideHeadPitches[i];
        }
        int j;
        for (i = 0; i < 2; ++i) {
            j = this.getTrackedEntityId(i + 1);
            Entity entity2 = null;
            if (j > 0) {
                entity2 = this.world.getEntityById(j);
            }
            if (entity2 != null) {
                double e = this.getHeadX(i + 1);
                double f = this.getHeadY(i + 1);
                double g = this.getHeadZ(i + 1);
                double h = entity2.getX() - e;
                double k = entity2.getEyeY() - f;
                double l = entity2.getZ() - g;
                double m = Math.sqrt(h * h + l * l);
                float n = (float) (MathHelper.atan2(l, h) * 57.2957763671875) - 90.0F;
                float o = (float) (-(MathHelper.atan2(k, m) * 57.2957763671875));
                this.sideHeadPitches[i] = this.getNextAngle(this.sideHeadPitches[i], o, 40.0F);
                this.sideHeadYaws[i] = this.getNextAngle(this.sideHeadYaws[i], n, 10.0F);
            } else {
                this.sideHeadYaws[i] = this.getNextAngle(this.sideHeadYaws[i], this.bodyYaw, 10.0F);
            }
        }
        boolean bl = this.shouldRenderOverlay();
        for (j = 0; j < 3; ++j) {
            double p = this.getHeadX(j);
            double q = this.getHeadY(j);
            double r = this.getHeadZ(j);
            this.world.addParticle(ParticleTypes.SMOKE, p + this.random.nextGaussian() * 0.30000001192092896, q + this.random.nextGaussian() * 0.30000001192092896, r + this.random.nextGaussian() * 0.30000001192092896, 0.0, 0.0, 0.0);
            if (bl && this.world.random.nextInt(4) == 0) {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, p + this.random.nextGaussian() * 0.30000001192092896, q + this.random.nextGaussian() * 0.30000001192092896, r + this.random.nextGaussian() * 0.30000001192092896, 0.699999988079071, 0.699999988079071, 0.5);
            }
        }
        if (this.getInvulnerableTimer() > 0) {
            for (j = 0; j < 3; ++j) {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.699999988079071, 0.699999988079071, 0.8999999761581421);
            }
        }
    }

    private float getNextAngle(float prevAngle, float desiredAngle, float maxDifference) {
        float f = MathHelper.wrapDegrees(desiredAngle - prevAngle);
        if (f > maxDifference) {
            f = maxDifference;
        }

        if (f < -maxDifference) {
            f = -maxDifference;
        }

        return prevAngle + f;
    }

    private void shootSkullAt(int headIndex, LivingEntity target) {
        this.shootSkullAt(headIndex, target.getX(), target.getY() + (double) target.getStandingEyeHeight() * 0.5, target.getZ(), headIndex == 0 && this.random.nextFloat() < 0.001F);
    }

    private void shootSkullAt(int headIndex, double targetX, double targetY, double targetZ, boolean charged) {
        if (!this.isSilent()) {
            this.world.syncWorldEvent(null, 1024, this.getBlockPos(), 0);
        }

        double d = this.getHeadX(headIndex);
        double e = this.getHeadY(headIndex);
        double f = this.getHeadZ(headIndex);
        double g = targetX - d;
        double h = targetY - e;
        double i = targetZ - f;
        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(this.world, this, g, h, i);
        witherSkullEntity.setOwner(this);
        if (charged) {
            witherSkullEntity.setCharged(true);
        }

        witherSkullEntity.setPos(d, e, f);
        this.world.spawnEntity(witherSkullEntity);
    }

    private double getHeadX(int headIndex) {
        if (headIndex <= 0) {
            return this.getX();
        } else {
            float f = (this.bodyYaw + (float) (180 * (headIndex - 1))) * 0.017453292F;
            float g = MathHelper.cos(f);
            return this.getX() + (double) g * 1.3;
        }
    }

    private double getHeadY(int headIndex) {
        return headIndex <= 0 ? this.getY() + 3.0 : this.getY() + 2.2;
    }

    private double getHeadZ(int headIndex) {
        if (headIndex <= 0) {
            return this.getZ();
        } else {
            float f = (this.bodyYaw + (float) (180 * (headIndex - 1))) * 0.017453292F;
            float g = MathHelper.sin(f);
            return this.getZ() + (double) g * 1.3;
        }
    }

    public void attack(LivingEntity target, float pullProgress) {
        this.shootSkullAt(0, target);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

    }

    public int getTrackedEntityId(int headIndex) {
        return this.dataTracker.get(TRACKED_ENTITY_IDS.get(headIndex));
    }

    public void setTrackedEntityId(int headIndex, int id) {
        this.dataTracker.set(TRACKED_ENTITY_IDS.get(headIndex), id);
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    public int getInvulnerableTimer() {
        return this.dataTracker.get(INVUL_TIMER);
    }

    public void setInvulTimer(int ticks) {
        this.dataTracker.set(INVUL_TIMER, ticks);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.WITHER && super.canHaveStatusEffect(effect);
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            this.discard();
        } else {
            this.despawnCounter = 0;
        }
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public static boolean canDestroy(BlockState block) {
        return !block.isAir() && !block.isIn(BlockTags.WITHER_IMMUNE);
    }

    public float getHeadPitch(int headIndex) {
        return this.sideHeadPitches[headIndex];
    }

    public float getHeadYaw(int headIndex) {
        return this.sideHeadYaws[headIndex];
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    public static DefaultAttributeContainer.Builder createImmortalWitherAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 1000.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6f).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0).add(EntityAttributes.GENERIC_ARMOR, 20.0);
    }
}
