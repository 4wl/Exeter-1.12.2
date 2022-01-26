package me.friendly.exeter.mixin;

import me.friendly.exeter.core.Exeter;
import me.friendly.exeter.events.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSubtitleOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSubtitleOverlay.class)
public abstract class MixinGuiSubtitleOverlay {
//    @Inject(
//        method = "renderSubtitles",
//        at = @At(value = "HEAD"))
//    private void renderSubtitlesHook(CallbackInfo info) {
//        Exeter.getInstance().getEventManager()
//                .dispatch(new Render2DEvent());
//        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
//    }

    @Inject(
        method = "renderSubtitles",
        at = @At(value = "HEAD"))
    private void renderSubtitlesHook(CallbackInfo info) {

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        RenderGameOverlayEvent overlayEvent
                = new RenderGameOverlayEvent(scaledResolution);

        Exeter.getInstance().getEventManager()
                .dispatch(overlayEvent);

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

}