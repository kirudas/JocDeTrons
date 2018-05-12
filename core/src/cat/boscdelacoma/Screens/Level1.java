package cat.boscdelacoma.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Iterator;

import cat.boscdelacoma.GestorContactes;
import cat.boscdelacoma.JocDeTrons;
import cat.boscdelacoma.MapBodyManager;
import cat.boscdelacoma.Moneda;
import cat.boscdelacoma.Ojo;
import cat.boscdelacoma.Personatge;
import cat.boscdelacoma.TiledMapHelper;

/**
 * La pantalla que controla el nivell 1 del joc
 *
 * @author Marc
 *
 */
public class Level1 extends AbstractScreen {

    //<editor-fold desc="Atributs">
    /**
     * Variable d'instancia que permet gestionar i pintar el mapa a partir d'un
     * TiledMap (TMX)
     */
    private TiledMapHelper tiledMapHelper;

    // objecte que gestiona el protagonista del joc
    private Personatge personatge;

    // Objecte que conté tots els cossos del joc als quals els aplica la simulació
    private World world;
    private ArrayList<Moneda> monedes;
    private int contMonedes;

    /**
     * Objecte que dibuixa elements per debugar. Dibuixa linies al voltant dels
     * límits de les col·lisions. Va molt bé per comprovar que les
     * col·lisions són les que desitgem. Cal tenir present, però, que és
     * més lent. Només s'ha d'utitilitzar per debugar.
     */
    private Box2DDebugRenderer debugRenderer;

    // Musica i sons
    private Music musica;

    // Per debugar les col·lisions
    private Box2DDebugRenderer box2DRenderer;

    // Per mostrar el títol
    private Label title;
    private Label score;
    private Label vides;

    private Table table = new Table();

    /**
     * per indicar quins cossos s'han de destruir
     */
    private ArrayList<Body> bodyDestroyList;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Level1(JocDeTrons joc) {
        super(joc);

        //title = new Label(joc.getTitol(),joc.getSkin(), "groc");
        score = new Label("0123", joc.getSkin(), "scoreStyle");
        vides = new Label("0",joc.getSkin(),"videsStyle");

		/*
		 * Crear el mon on es desenvolupa el joc.
		 * S'indica la gravetat: negativa perquè indica cap avall
		 */
        world = new World(new Vector2(0.0f, -9.8f), true);

        carregarMapa();
        carregarObjectes();
        carregarMusica();

        // --- si es volen destruir objectes, descomentar ---
        bodyDestroyList= new ArrayList<Body>();
        world.setContactListener(new GestorContactes(bodyDestroyList));
        //world.setContactListener(new GestorContactes());

        // crear el personatge
        personatge = new Personatge(world);
        monedes = new ArrayList<Moneda>();
        monedes.add(new Moneda(world,2f,3f));
        monedes.add(new Moneda(world,25f,2f));
        monedes.add(new Moneda(world,47.5f,3f));
        monedes.add(new Moneda(world,59f,5f));
        monedes.add(new Moneda(world,84f,6f));
        monedes.add(new Moneda(world,120f,5f));
        monedes.add(new Moneda(world,165f,9f));


        contMonedes = 0;
        // objecte que permet debugar les col·lisions
        //debugRenderer = new Box2DDebugRenderer();



    }
    //</editor-fold>

    //<editor-fold desc="Mètodes d'instància">
    /**
     * Moure la càmera en funció de la posició del personatge
     */
    private void moureCamera() {
        // Posicionem la camera centran-la on hi hagi l'sprite del protagonista
        tiledMapHelper.getCamera().position.x = JocDeTrons.PIXELS_PER_METRE
                * personatge.getPositionBody().x;

        // Assegurar que la camera nomes mostra el mapa i res mes
        if (tiledMapHelper.getCamera().position.x <  joc.getScreenWidth() / 2) {
            tiledMapHelper.getCamera().position.x =  joc.getScreenWidth()/ 2;
        }
        if (tiledMapHelper.getCamera().position.x >= tiledMapHelper.getWidth()
                -  joc.getScreenWidth()/ 2) {
            tiledMapHelper.getCamera().position.x = tiledMapHelper.getWidth()
                    - joc.getScreenWidth()/ 2;
        }

        if (tiledMapHelper.getCamera().position.y < joc.getScreenHeight() / 2) {
            tiledMapHelper.getCamera().position.y = joc.getScreenHeight()/ 2;
        }
        if (tiledMapHelper.getCamera().position.y >= tiledMapHelper.getHeight()
                - joc.getScreenHeight() / 2) {
            tiledMapHelper.getCamera().position.y = tiledMapHelper.getHeight()
                    - joc.getScreenHeight() / 2;
        }

        // actualitzar els nous valors de la càmera
        tiledMapHelper.getCamera().update();
    }

    /**
     * tractar els events de l'entrada
     */
    private void tractarEventsEntrada() {
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            personatge.setMoureDreta(true);
        } else {
            for (int i = 0; i < 2; i++) {
                if (Gdx.input.isTouched(i)
                        && Gdx.input.getX() > Gdx.graphics.getWidth() * 0.80f) {
                    personatge.setMoureDreta(true);
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            personatge.setMoureEsquerra(true);
        } else {
            for (int i = 0; i < 2; i++) {
                if (Gdx.input.isTouched(i)
                        && Gdx.input.getX() < Gdx.graphics.getWidth() * 0.20f) {
                    personatge.setMoureEsquerra(true);
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            personatge.setFerSalt(true);
        } else {
            for (int i = 0; i < 2; i++) {
                if (Gdx.input.isTouched(i)
                        && Gdx.input.getY() < Gdx.graphics.getHeight() * 0.20f) {
                    personatge.setFerSalt(true);
                }
            }
        }
    }

    /**
     * Carrega el mapa del joc a partir d'un fitxer TMX
     */
    private void carregarMapa() {
        tiledMapHelper = new TiledMapHelper();
        tiledMapHelper.setPackerDirectory("world/level2/packer");
        tiledMapHelper.loadMap("world/level2/packer/level2.tmx");
        tiledMapHelper.prepareCamera(joc.getScreenWidth(), joc.getScreenHeight());
    }

    /**
     * Carregar i reproduir l'arxiu de música de fons
     */
    public void carregarMusica() {
        musica = Gdx.audio.newMusic(Gdx.files.internal("sons/gameOfThrones.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);
        musica.play();
    }

    /**
     * Càrrega dels objectes que defineixen les col·lisions
     */
    private void carregarObjectes() {
        MapBodyManager mapBodyManager = new MapBodyManager(world,
                JocDeTrons.PIXELS_PER_METRE,
                Gdx.files.internal("world/level1/materials.json"), 1);
        mapBodyManager.createPhysics(tiledMapHelper.getMap(), "Box2D");
    }
    //</editor-fold>

    //<editor-fold desc="Mètodes sobreescrits">
    @Override
    public void render(float delta) {
        personatge.inicialitzarMoviments();
        tractarEventsEntrada();
        personatge.moure();
        personatge.updatePosition();
        /**
         * Cal actualitzar les posicions i velocitats de tots els objectes. El
         * primer paràmetre és la quanitat de frames/segon que dibuixaré
         * El segon i tercer paràmetres indiquen la quantitat d'iteracions per
         * la velocitat i per tractar la posició. Un valor alt és més
         * precís però més lent.
         */
        world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);
		/*
		 * per destruir cossos marcats per ser eliminats
		 */
        	for(int i = bodyDestroyList.size()-1; i >=0; i-- ) {
		    	world.destroyBody(bodyDestroyList.get(i));
		}
		bodyDestroyList.clear();


        // Esborrar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Color de fons marro
        Gdx.gl.glClearColor(22f / 255f, 46f / 255f, 26f / 255f, 0);

        moureCamera();

        // pintar el mapa
        tiledMapHelper.render();

        // Preparar l'objecte SpriteBatch per dibuixar la resta d'elements
        batch.setProjectionMatrix(tiledMapHelper.getCamera().combined);

        // iniciar el lot
        batch.begin();
        personatge.dibuixar(batch);
        Iterator<Moneda> i = monedes.iterator();
        while(i.hasNext()){
            Moneda moneda = i.next();
            if (moneda.isVisible())
                moneda.draw(batch);
            else
                contMonedes++;
        }
        //carregar seguent món
        /*if(monedes.size() == contMonedes)
            ((Game)Gdx.app.getApplicationListener()).setScreen(new Level2(getGame()));*/
        //joc.getSkin().getFont("scoreFont").draw(batch, "0123", 10, 10);

        // finalitzar el lot: a partir d'aquest moment es dibuixa tot el que
        // s'ha indicat entre begin i end
        batch.end();

        score.setText(Long.toString(personatge.getPunts()));
        vides.setText(Long.toString(personatge.getVides()));

        calculRedimensionat();

        // dibuixar els controls de pantalla
        stage.act();
        stage.draw();
        /*debugRenderer.render(world, tiledMapHelper.getCamera().combined.scale(
                JocDeTrons.PIXELS_PER_METRE, JocDeTrons.PIXELS_PER_METRE,
                JocDeTrons.PIXELS_PER_METRE));*/
    }



    @Override
    public void dispose() {
        musica.stop();
        musica.dispose();
        world.dispose();
        personatge.dispose();
    }

    public void show() {
        // Els elements es mostren en l'ordre que s'afegeixen.
        // El primer apareix a la part superior, el darrer a la part inferior.
        table.left().top();
        table.add(score).pad(10);
        table.add(vides).pad(15);
        table.setFillParent(true);
        stage.addActor(table);
    }
    //</editor-fold>
}
