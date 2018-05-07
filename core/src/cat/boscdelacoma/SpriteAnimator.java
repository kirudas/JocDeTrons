package cat.boscdelacoma;

/**
 * Created by Joan on 27/04/2018.
 */


        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimator {

    // Objects used
    Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)

    private TextureRegion[] frames;
    private Texture frame;
    private Sprite sprite;

    // A variable for tracking elapsed time for the animation
    float stateTime;

    public SpriteAnimator(Sprite sprite, int textureCols, int textureRows) {

        // Load the sprite sheet as a Texture
        Texture framesTexture = sprite.getTexture();

        this.sprite = sprite;

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(framesTexture,
                framesTexture.getWidth() / textureCols,
                framesTexture.getHeight() / textureRows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] frames = new TextureRegion[textureCols * textureRows];
        int index = 0;
        for (int i = 0; i < textureRows; i++) {
            for (int j = 0; j < textureCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        animation = new Animation<TextureRegion>(0.25f, frames);
    }

    public void draw(SpriteBatch spriteBatch) {

        stateTime += Gdx.graphics.getDeltaTime()* 2; // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        //Draw current frame
        spriteBatch.draw((TextureRegion) currentFrame, sprite.getX(), sprite.getY());
    }
}









































