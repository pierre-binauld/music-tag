package binauld.pierre.musictag.service.action;


import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class FilenamesBuilderAction implements Runnable, ItemVisitor{

    private StringBuilder builder;

    public FilenamesBuilderAction(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void visit(AudioFile audioFile) {
        builder.append(audioFile.getFile().getAbsolutePath());
        builder.append("\n");
    }

    @Override
    public void visit(Folder folder) {

    }

    @Override
    public void run() {
        builder.deleteCharAt(builder.length() - 1);
    }
}
