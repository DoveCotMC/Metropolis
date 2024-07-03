package team.dovecotmc.metropolis.mixins;

import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(ItemGroup.class)
public class MixinItemGroup {
//    @Inject(at = @At("TAIL"), method = "appendStacks")
//    public void appendStacksTail(DefaultedList<ItemStack> stacks, CallbackInfo ci) {
//        Iterator var2 = Registry.ITEM.iterator();
//
//        System.out.println(114514);
//        while(var2.hasNext()) {
//            Item item = (Item)var2.next();
//            if (item instanceof ItemTurnstile) {
//                System.out.println(item.getGroup().getDisplayName());
//            }
//        }
//    }
}
