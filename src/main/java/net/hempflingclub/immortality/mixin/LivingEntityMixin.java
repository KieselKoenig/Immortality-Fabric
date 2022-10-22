package net.hempflingclub.immortality.mixin;

import net.hempflingclub.immortality.event.PlayerKillEntityCallback;
import net.hempflingclub.immortality.util.ImmortalityInvokeImmortality;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<? extends Entity> type, World world) {
        super(type, world);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onKilledBy(Lnet/minecraft/entity/LivingEntity;)V"),
            method = "onDeath", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onKilledByEntity(DamageSource source, CallbackInfo ci, Entity entity) {
        if (entity instanceof ServerPlayerEntity player) {
            PlayerKillEntityCallback.EVENT.invoker().killEntity(player, this);
        }
    }

    @ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;getHealth()F"), ordinal = 0, argsOnly = true)
    protected float immortality$immortality(float damageAmount, DamageSource dmgSource) {
        if (this.isLiving()) {
            //Determine if Player should die / get yeeted to their Bed/WorldSpawn and also to invoke Immortality rightly
            return ImmortalityInvokeImmortality.damageManager((LivingEntity) (Entity) this, dmgSource, damageAmount);
        }
        return damageAmount;
    }
}
