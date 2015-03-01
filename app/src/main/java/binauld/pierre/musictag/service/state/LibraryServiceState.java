package binauld.pierre.musictag.service.state;


import java.util.List;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LoadingState;

public interface LibraryServiceState {

    public void switchComposite(LibraryComposite composite);

    public boolean backToParentComposite();

    public int getComponentMaxChildrenCount();

    public int getCurrentProgress();

    public LoadingState getCurrentLoadingState();

    public boolean hasParent();

    public String getComponentName();

    public List<LibraryComponent> getCurrentComponentList();

    public void loadCurrentComposite(boolean drillDown, Runnable callback);
}
