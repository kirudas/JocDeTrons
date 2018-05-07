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
 * Classe que implementa el protagonista del joc
 */
public class Personatge {

    //<editor-fold desc="Constants">
    public static final int FRAME_COLS = 9;
    public static final int FRAME_ROWS = 2;
    //</editor-fold>

    //<editor-fold desc="Atributs">


    /**
     * Detectar el moviment
     */
    private boolean moureEsquerra;
    private boolean moureDreta;
    private boolean ferSalt;
    private boolean personatgeCaraDreta;

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spritePersonatge;    // sprite associat al personatge
    private AnimatedSprite spriteAnimat;// animació de l'sprite
    private Texture stoppedTexture;     // la seva textura
    private Sound soSalt;               // el so que reprodueix en saltar
    private Texture animatedTexture;
    private long idSoSalt;

    private long punts;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Personatge(World world) {
        moureEsquerra = moureDreta = ferSalt = false;
        this.world = world;
        carregarTextures();
        carregarSons();
        crearProtagonista();
        punts = 0;
    }
    //</editor-fold>

    //<editor-fold desc="Mètordes privats">
    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal("imatges/warriorSpriteSheet.png"));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stoppedTexture = new Texture(Gdx.files.internal("imatges/warrior.png"));
        stoppedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    /**
     * Carregar els arxius de so
     */
    private void carregarSons() {
        soSalt = Gdx.audio.newSound(Gdx.files.internal("sons/salt.mp3"));
    }

    private void crearProtagonista() {
        spritePersonatge = new Sprite(animatedTexture);
        spriteAnimat = new AnimatedSprite(spritePersonatge, FRAME_COLS, FRAME_ROWS, stoppedTexture);

        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.DynamicBody;
        defCos.position.set(1.0f, 3.0f);

        cos = world.createBody(defCos);
        cos.setUserData(this);
        /**
         * Definir les vores de l'sprite per indicar on col·lisiona
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox((spritePersonatge.getWidth() / FRAME_COLS) / (2 * JocDeTrons.PIXELS_PER_METRE),
                (spritePersonatge.getHeight() / FRAME_ROWS) / (2 * JocDeTrons.PIXELS_PER_METRE));

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
        requadre.dispose();
    }
    //</editor-fold>

    //<editor-fold desc="Mètodes d'instància">
    public void inicialitzarMoviments() {
        setMoureDreta(false);
        setMoureEsquerra(false);
        setFerSalt(false);
        spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED_FACE_RIGHT);
        setCaraDreta(false);
    }

    /**
     * Actualitza la posició de l'sprite
     */
    public void updatePosition() {
        spritePersonatge.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spritePersonatge.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spritePersonatge.getHeight() / FRAME_ROWS / 2);
        spriteAnimat.setPosition(spritePersonatge.getX(), spritePersonatge.getY());
    }

    public void dibuixar(SpriteBatch batch) {
        spriteAnimat.draw(batch);
    }

    /**
     * Fer que el personatge es mogui
     *
     * Canvia la posició del protagonista
     * Es tracta de forma separada el salt perquè es vol que es pugui moure si salta
     * al mateix temps.
     *
     * Els impulsos s'apliquen des del centre del protagonista
     */
    public void moure() {

        if(spriteAnimat.getDirection() == AnimatedSprite.Direction.STOPPED_FACE_RIGHT) {
            if(moureEsquerra) {
                spriteAnimat.setDirection(AnimatedSprite.Direction.STOPPED_FACE_LEFT);
            }
        }

        if (moureDreta) {
            cos.setLinearVelocity(2.5f, cos.getLinearVelocity().y);
            spriteAnimat.setDirection(AnimatedSprite.Direction.RIGHT);

            if (!personatgeCaraDreta) {
                spritePersonatge.flip(true, false);
            }
            personatgeCaraDreta = true;
        } else if (moureEsquerra) {
            cos.setLinearVelocity(-2.5f, cos.getLinearVelocity().y);
            spriteAnimat.setDirection(AnimatedSprite.Direction.LEFT);
            if (personatgeCaraDreta) {
                spritePersonatge.flip(true, false);
            }
            personatgeCaraDreta = false;
        }

        if (ferSalt && Math.abs(cos.getLinearVelocity().y) < 1e-9) {
            cos.applyLinearImpulse(new Vector2(0.0f, 3.0f),
                    cos.getPosition(), true);
            idSoSalt = soSalt.play();
            //punts++;
        }
    }


    /**
     * Destructor
     */
    public void dispose() {
        animatedTexture.dispose();
        stoppedTexture.dispose();
        soSalt.dispose();
    }
    //</editor-fold>

    //<editor-fold desc="Getters / Setters">
    public boolean isMoureEsquerra() {
        return moureEsquerra;
    }

    public void setMoureEsquerra(boolean moureEsquerra) {
        this.moureEsquerra = moureEsquerra;
    }

    public boolean isMoureDreta() {
        return moureDreta;
    }

    public void setMoureDreta(boolean moureDreta) {
        this.moureDreta = moureDreta;
    }

    public boolean isFerSalt() {
        return ferSalt;
    }

    public void setFerSalt(boolean ferSalt) {
        this.ferSalt = ferSalt;
    }

    public boolean isCaraDreta() {
        return this.personatgeCaraDreta;
    }

    public void setCaraDreta(boolean caraDreta) {
        this.personatgeCaraDreta = caraDreta;

    }

    public Sound getSoSalt() {
        return soSalt;
    }

    public void setSoSalt(Sound soSalt) {
        this.soSalt = soSalt;
    }

    // Retorna la posició del cos associat al protagonista
    public Vector2 getPositionBody() {
        return this.cos.getPosition();
    }

    // Retorna la posició de l'sprite associat al protagonista
    public Vector2 getPositionSprite() {
        return new Vector2().set(this.spritePersonatge.getX(), this.spritePersonatge.getY());
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

    public void afegirPunt(){this.punts++;}

    //</editor-fold>

}
