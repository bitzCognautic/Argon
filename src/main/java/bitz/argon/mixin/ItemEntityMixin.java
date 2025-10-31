package bitz.argon.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    
    @Shadow
    public abstract ItemStack getStack();
    
    @Shadow
    private int itemAge;
    
    @Inject(method = "tryMerge()V", at = @At("HEAD"))
    private void onTryMerge(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        
        // Increase merge range for better item stacking
        double mergeRange = 2.5; // Increased from vanilla's 0.5
        
        for (ItemEntity other : self.getEntityWorld().getEntitiesByClass(
                ItemEntity.class,
                self.getBoundingBox().expand(mergeRange),
                (entity) -> entity != self && !entity.isRemoved()
        )) {
            if (canMerge(self, other)) {
                ItemStack selfStack = self.getStack();
                ItemStack otherStack = other.getStack();
                
                int combinedCount = selfStack.getCount() + otherStack.getCount();
                int maxCount = selfStack.getMaxCount() * 64; // Allow stacking beyond normal limits
                
                if (combinedCount <= maxCount) {
                    selfStack.setCount(combinedCount);
                    other.discard();
                    self.setStack(selfStack);
                } else {
                    selfStack.setCount(maxCount);
                    otherStack.setCount(combinedCount - maxCount);
                    self.setStack(selfStack);
                    other.setStack(otherStack);
                }
            }
        }
    }
    
    private boolean canMerge(ItemEntity self, ItemEntity other) {
        ItemStack selfStack = self.getStack();
        ItemStack otherStack = other.getStack();
        
        return !selfStack.isEmpty() && 
               !otherStack.isEmpty() && 
               ItemStack.areItemsAndComponentsEqual(selfStack, otherStack);
    }
}
