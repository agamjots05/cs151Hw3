package mvc;
import java.io.Serializable;

public class Model extends Publisher implements Serializable {

    private String fileName = null;
    private Boolean unsavedChanges = false;

    protected String getFileName(){
        return this.fileName;
    }

    protected void setFileName(String newFileName){
        this.fileName = newFileName;
    }

    protected Boolean getUnsavedChanges(){
        return this.unsavedChanges;
    }

    protected void setUnsavedChanges(Boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    protected void changed(){
        setUnsavedChanges(true);
        notifySubscribers();
    }
}
