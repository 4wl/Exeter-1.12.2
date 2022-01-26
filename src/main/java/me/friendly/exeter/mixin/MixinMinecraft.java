package me.friendly.exeter.mixin;

import me.friendly.exeter.core.Exeter;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft// implements IMinecraft
{
//    @Override
//    @Accessor(value = "rightClickDelayTimer")
//    public abstract int getRightClickDelay();
//
//    @Override
//    @Accessor(value = "rightClickDelayTimer")
//    public abstract void setRightClickDelay(int delay);
//
//    @Override
//    @Accessor(value = "metadataSerializer")
//    public abstract MetadataSerializer getMetadataSerializer();
//
//    @Override
//    @Accessor(value = "timer")
//    public abstract Timer getTimer();

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V",
                    ordinal = 2,
                    shift = At.Shift.BEFORE))
    private void initHook2(CallbackInfo ci)
    {
        new Exeter();
//        Exeter.getInstance().init();
//        Earthhack.postInit();
    }
}
