package binauld.pierre.musictag.collection;



import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.visitor.ComponentVisitor;

/**
 * Allows to compare 2 library items.
 * Sort files in alphabetical order (Folder first).
 */
public class LibraryItemComparator implements Comparator<LibraryComponent> {

    private ComponentValueVisitor visitor1 = new ComponentValueVisitor();
    private ComponentValueVisitor visitor2 = new ComponentValueVisitor();

    @Override
    public int compare(LibraryComponent component1, LibraryComponent component2) {
//TODO: search for item downcast
        component1.accept(visitor1);
        component2.accept(visitor2);

        int comparison = visitor2.getComponentValue() - visitor1.getComponentValue();

        if(0 != comparison) {
            return comparison;
        } /*else if (item1.isAudioItem() && !item2.isAudioItem()) {
            return 1;
        } */else {
            String string1 = StringUtils.stripAccents(component1.getItem().getPrimaryInformation()).toLowerCase();
            String string2 = StringUtils.stripAccents(component2.getItem().getPrimaryInformation()).toLowerCase();
            return string1.compareTo(string2);
        }
    }

    class ComponentValueVisitor implements ComponentVisitor {

        private int componentValue;

        @Override
        public void visit(LibraryLeaf leaf) {
            componentValue = 0;
        }

        @Override
        public void visit(LibraryComposite composite) {
            componentValue = 1;
        }

        public int getComponentValue() {
            return componentValue;
        }
    }
}
