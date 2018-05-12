package cat.boscdelacoma;

/**
 * Created by Joan on 27/04/2018.
 */


        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.audio.Sound;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.physics.box2d.Body;
        import com.badlogic.gdx.physics.box2d.BodyDef;
        import com.badlogic.gdx.physics.box2d.FixtureDef;
        import com.badlogic.gdx.physics.box2d.PolygonShape;
        import com.badlogic.gdx.physics.box2d.World;

public class Moneda {

    //<editor-fold desc="Constants">
    public static final int FRAME_COLS = 10;
    public static final int FRAME_ROWS = 1;
    public static final float REL_MONEDA_MAPA = 4; // la mida de la moneda es de 32px i els quadrats
                                                    // del mapa es de 128px
    //</editor-fold>

    private World world;                // Referència al mon on està definit el personatge
    private Body cos;                   // per definir les propietats del cos
    private Sprite spriteMoneda;        // sprite associat a la moneda
    private SpriteAnimator spriteAnimat;// animació de l'sprite
    private Texture animatedTexture;
    private int frameWidth;
    private int frameHeight;
    private Sound soMoneda;             // el so que reprodueix en desapareixer una moneda
    private float x, y;                 //Posició de la moneda
    private boolean isVisible = true;

    //<editor-fold desc="Constructors">
    public Moneda(World world, float x, float y) {

        this.world = world;
        this.x = x;
        this.y = y;
        carregarTextures();
        carregarSons();
        crearMoneda();
    }

    private void carregarSons() {
        soMoneda = Gdx.audio.newSound(Gdx.files.internal("sons/coin.mp3"));
    }
    //</editor-fold>

    //<editor-fold desc="Mètordes privats">
    private void carregarTextures() {
        animatedTexture = new Texture(Gdx.files.internal("imatges/monedaSprt2.png"));
        animatedTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        frameWidth = animatedTexture.getWidth()/FRAME_COLS;
        frameHeight = animatedTexture.getHeight()/FRAME_ROWS;
    }


    private void crearMoneda() {
        spriteMoneda = new Sprite(animatedTexture);
        //spriteMoneda.setScale(0.5f);        //Reduim la mida de la moneda
        spriteAnimat = new SpriteAnimator(spriteMoneda, FRAME_COLS, FRAME_ROWS);
        // Definir el tipus de cos i la seva posició
        BodyDef defCos = new BodyDef();
        defCos.type = BodyDef.BodyType.StaticBody;
        defCos.position.set(x, y);

        cos = world.createBody(defCos);
        cos.setUserData(this);

        /**
         * Definir les vores de l'sprite per indicar on col·lisiona
         */
        PolygonShape requadre = new PolygonShape();
        requadre.setAsBox(frameWidth / (2 * JocDeTrons.PIXELS_PER_METRE),
                frameHeight / (2 * JocDeTrons.PIXELS_PER_METRE));

        /**
         * La densitat i fricció de la moneda. Si es modifiquen aquests
         * valor anirà més ràpid o més lent.
         */
        FixtureDef propietats = new FixtureDef();
        propietats.shape = requadre;
        propietats.density = 10.0f;
        propietats.friction = 0f;

        cos.setFixedRotation(true);
        cos.createFixture(propietats);
        requadre.dispose();

        //Definim la posicio de la moneda
        definirPosicio();
    }

    private void definirPosicio(){
        spriteMoneda.setPosition(
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().x
                        - spriteMoneda.getWidth() / FRAME_COLS / 2,
                JocDeTrons.PIXELS_PER_METRE * cos.getPosition().y
                        - spriteMoneda.getHeight() / FRAME_ROWS / 2);

    }

    public void draw(SpriteBatch batch) {
        if (!isVisible){
            soMoneda.play();
        }
        definirPosicio();
        spriteAnimat.draw(batch);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {

        isVisible = visible;
    }

}