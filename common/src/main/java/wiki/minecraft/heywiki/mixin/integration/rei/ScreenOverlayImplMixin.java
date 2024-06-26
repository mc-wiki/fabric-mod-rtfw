package wiki.minecraft.heywiki.mixin.integration.rei;

import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wiki.minecraft.heywiki.HeyWikiClient;
import wiki.minecraft.heywiki.wiki.Target;
import wiki.minecraft.heywiki.wiki.WikiPage;

import java.util.Objects;

@Mixin(EntryWidget.class)
public abstract class ScreenOverlayImplMixin {
    @Shadow(remap = false)
    public abstract EntryStack<?> getCurrentEntry();

    // This is so hacky
    @Inject(method = "keyPressedIgnoreContains", at = @At("HEAD"), remap = false)
    public void keyPressedIgnoreContains(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (REIRuntime.getInstance().isOverlayVisible()) {
            EntryStack<?> stack = this.getCurrentEntry();
            if (stack != null && !stack.isEmpty()) {
                stack = stack.copy();
                if (HeyWikiClient.openWikiKey.matchesKey(keyCode, scanCode)) {
                    if (stack.getValue() instanceof ItemStack itemStack) {
                        var target = Target.of(itemStack);
                        if (target != null)
                            Objects.requireNonNull(WikiPage.fromTarget(target)).openInBrowser();
                    }
                }
            }
        }
    }
}
