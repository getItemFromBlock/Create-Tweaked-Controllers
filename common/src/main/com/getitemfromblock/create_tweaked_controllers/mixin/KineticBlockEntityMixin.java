package com.getitemfromblock.create_tweaked_controllers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

@Mixin(KineticBlockEntity.class)
public class KineticBlockEntityMixin
{
    @Inject(method = "getFlickerScore", at = @At("HEAD"), cancellable = true, remap = false)
    private void getFlickerScoreMixin(CallbackInfoReturnable<Integer> callback)
    {
        callback.setReturnValue(0);
    }
}
