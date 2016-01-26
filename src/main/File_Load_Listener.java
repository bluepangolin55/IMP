package main;

import java.util.EventListener;
import file.Img;

public interface File_Load_Listener extends EventListener{

    public void new_image_loaded(Img new_image);
	
    public void image_changed();
    
    public void selection_changed();
    
    public void preview_changed();
}
