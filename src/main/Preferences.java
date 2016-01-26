package main;

public class Preferences {

    //preferences !!!!!!!!
    private boolean Automatic_preview_when_filter=true;
    
    public boolean menubar_on=false;

	public boolean isAutomatic_preview_when_filter() {
		return Automatic_preview_when_filter;
	}

	public void setAutomatic_preview_when_filter(
			boolean automatic_preview_when_filter) {
		Automatic_preview_when_filter = automatic_preview_when_filter;
	}
}
