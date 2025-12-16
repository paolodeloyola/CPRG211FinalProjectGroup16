public abstract class BaseEntity {
    private final int publicId;

    public BaseEntity(int publicId) {
        this.publicId = publicId;
    }

    public int getPublicId() {
        return publicId;
    }
}
