package cat.boscdelacoma;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Joan on 20/04/2018.
 */

public class Ojo {
    //<editor-fold desc="Constants">
    public static final int FRAME_COLS = 16;
    public static final int FRAME_ROWS = 1;
    //</editor-fold>
    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    //private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    //private Texture animatedTexture;
    private long idSoSalt;

    private long punts;
    //</editor-fold>

    public Ojo(World world) {
        this.world = world;
        carregarTextures();
        crearProtagonista();
        punts = 0;
    }
    private void carregarTextures() {
        //animatedTexture = new Texture(Gdx.files.internal("imatges/ojoSpriteSheet.png"));
        //animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal("imatges/ojo.png"));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    private void crearProtagonista() {
        //spritePersonatge = new Sprite(animatedTexture);
        //spriteAnimat = new AnimatedSprite(spritePersonatge, FRAME_COLS, FRAME_ROWS, stoppedTexture);

        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(6.0f, 3.0f);

        cos = world.createBody(defCos);
        cos.setUserData("Ull");
        /**
         * Definir les vores de l'sprite per indicar on col·lisiona
         */
        PolygonShape requadre = new PolygonShape();
        Sprite aux = new Sprite(stoppedTexture);
        requadre.setAsBox((aux.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (aux.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricció del protagonista. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 1.0f;
        propietats.friction = 3.0f;

        cos.setFixedRotation(true);
        cos.createFixture(propietats);
        //requadre.dispose();
        //spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);
    }
    public void inicialitzarMoviments() {
        //spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED_FACE_RIGHT);
    }

    /**
     * Actualitza la posició de l'sprite
     */
    public void updatePosition() {
        /*spritePersonatge.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spritePersonatge.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spritePersonatge.getHeight() / FRAME_ROWS / 2);
        spriteAnimat.setPosition(spritePersonatge.getX(), spritePersonatge.getY());*/
    }

    public void dibuixar(SpriteBatch batch) {
        spriteAnimat.draw(batch);
    }
    /**
     * Destructor
     */
    public void dispose() {
        //animatedTexture.dispose();
        stoppedTexture.dispose();
    }

    // Retorna la posició del cos associat al protagonista
    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    // Retorna la posició de l'sprite associat al protagonista
    public Vector2 getPositionSprite() {
        return new Vector2(6.0f,3.0f);//return new Vector2().set(this.spritePersonatge.getX(), this.spritePersonatge.getY());
    }

    public Texture getTextura() {
        return stoppedTexture;
    }

    public void setTextura(Texture textura) {
        this.stoppedTexture = textura;
    }

    public long getPunts() {
        return punts;
    }

    public void setPunts(long punts) {
        this.punts = punts;
    }
}
