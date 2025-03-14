package mvc;
import java.io.Serializable;

public class Model extends Publisher implements Serializable {

    private String fileName = null;
    private Boolean unsavedChanges = false;

    public String getFileName(){
        return this.fileName;
    }

    public void setFileName(String newFileName){
        this.fileName = newFileName;
    }

    public Boolean getUnsavedChanges(){
        return this.unsavedChanges;
    }

    public void setUnsavedChanges(Boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public void changed(){
        setUnsavedChanges(true);
        notifySubscribers();
    }
}
