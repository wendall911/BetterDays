package betterdays.message;

public enum ChatTypeOptions {
    SYSTEM(false),
    GAME_INFO(true);

    private final boolean overlay;

    ChatTypeOptions(boolean overlay) {
        this.overlay = overlay;
    }

    public boolean isOverlay() {
        return overlay;
    }
}
