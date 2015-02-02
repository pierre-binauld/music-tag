package binauld.pierre.musictag.visitor.impl;


import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;

public abstract class ComponentVisitors {

    public static ComponentVisitor buildDrillDownComponentVisitor(ItemVisitor itemVisitor) {
        return new DrillDownComponentVisitor(itemVisitor);
    }

    public static ComponentVisitor buildRestrictedScopeComponentVisitor(ItemVisitor itemVisitor) {
        return new RestrictedScopeComponentVisitor(itemVisitor);
    }
}
