package net.hempflingclub.immortality.mixin;


import net.hempflingclub.immortality.util.ImmortalityEnvokeImmortality;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerImmortalityMixin extends LivingEntity {
    protected PlayerImmortalityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //Inject in applied Damage
    @ModifyVariable(method = "applyDamage", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F"), ordinal = 0, argsOnly = true)
    protected float immortality$immortality(float damageAmount, DamageSource dmgSource) {
        //Determine if Player should die / get yeeted to their Bed/WorldSpawn and also to envoke Immortality rightly
        return ImmortalityEnvokeImmortality.damageManager(this, dmgSource, damageAmount);
    }
}
