package mvc;
import java.io.Serializable;

public abstract class Model extends Publisher implements Serializable {

    private String fileName = null;
    private Boolean unsavedChanges = false;

    private String getFileName(){
        return this.fileName;
    }

    private void setFileName(String newFileName){
        this.fileName = newFileName;
    }

    private Boolean getUnsavedChanges(){
        return this.unsavedChanges;
    }

    private void setUnsavedChanges(Boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public void changed(){
        setUnsavedChanges(true);
        notifySubscribers();
    }
}
