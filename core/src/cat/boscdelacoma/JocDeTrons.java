package cat.boscdelacoma;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import cat.boscdelacoma.Screens.SplashScreen;

/**
 * classe que implementa la classe principal del joc
 */
public class JocDeTrons extends Game {

	//<editor-fold desc="Constants">
	/**
	 * Box2D treballa millor amb valors petits. NO s'han d'utilitzar unitats de
	 * píxels. Es recomana utilitzar una constant per convertir píxels a metres
	 * i a la inversa
	 */
	public static final float PIXELS_PER_METRE = 90.0f;

	public static final int WIDTH = 2600;
	public static final int HEIGHT = 1480;
	//</editor-fold>

	//<editor-fold desc="Atributs">
	// conté el títol del joc
	private String titol;
	// skin utilitzat en el joc
	private Skin skin;

	/**
	 * Mides de la pantalla en píxels
	 */
	private int screenWidth;
	private int screenHeight;

	private BitmapFont scoreFont;
	private BitmapFont defaultFont;
	//</editor-fold>

	//<editor-fold desc="Constructors">
	/**
	 * Constructor per defecte
	 */
	public JocDeTrons() {
		setScreenWidth(JocDeTrons.WIDTH);
		setScreenHeight(JocDeTrons.HEIGHT);
	}

	/**
	 * Constructor amb paràmetres
	 *
	 * @param width
	 *            Amplada de la finestra
	 * @param height
	 *            Alçada de la finestra
	 */
	public JocDeTrons(int width, int height, String titol) {
		this(titol);
		setScreenWidth(width);
		setScreenHeight(height);
	}

	public JocDeTrons(String titol) {
		this();
		this.setTitol(titol);
	}
	//</editor-fold>

	//<editor-fold desc="Cicle de vida del joc">
	@Override
	public void create() {

		loadSkin();

		// si està en un dispositiu Android, escalar la font segons la densitat de pantalla
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			skin.getFont("defaultFont").getData().setScale(Gdx.graphics.getDensity(), Gdx.graphics.getDensity());
			skin.getFont("scoreFont").getData().setScale(Gdx.graphics.getDensity(), Gdx.graphics.getDensity());
		}

		// començar el joc amb la SplashScreen
		setScreen(new SplashScreen(this));
	}


	@Override
	public void resume() {

	}

	@Override
	public void render() {
		super.render();

	}

	@Override
	public void dispose() {
		super.dispose();
		skin.dispose();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
	}
	//</editor-fold>

	//<editor-fold desc="Getters / Setters">
	public String getTitol() {
		return titol;
	}

	public void setTitol(String titol) {
		this.titol = titol;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}
	//</editor-fold>


	//<editor-fold desc="Metodes privats">
	private void loadFonts() {
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/DK Pundak.otf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 16;
		params.borderWidth = 1;
		params.color = Color.WHITE;
		scoreFont = gen.generateFont(params);
		gen.dispose();

		gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Game of Thrones.ttf"));
		params.size = 32;
		params.borderWidth = 2;
		params.borderColor = Color.WHITE;
		params.color = Color.GREEN;
		defaultFont = gen.generateFont(params);
		gen.dispose();
	}

	private void loadSkin() {

		loadFonts();

		// càrrega de l'skin
		//skin = new Skin(Gdx.files.internal("skins/skin.json"));
		skin = new Skin();
		// add colors
		Color yellow = new Color(1, 0.75f, 0, 1);
		Color green =  new Color(0, 1, 0, 1);
		Color white =  new Color(1, 1, 1, 1);

		skin.add("black", new Color(0, 0, 0, 1), Color.class);
		skin.add("green", green, Color.class);
		skin.add("yellow", yellow, Color.class);
		skin.add("white", white, Color.class);

		// add fonts
		skin.add("scoreFont", scoreFont, BitmapFont.class);
		//defaultFont = new BitmapFont(Gdx.files.internal("fonts/fontGoT.fnt"));
		skin.add("defaultFont", defaultFont, BitmapFont.class);

		// add styles
		skin.add("defaultStyle", new Label.LabelStyle(defaultFont, green ), Label.LabelStyle.class);
		skin.add("yellowStyle", new Label.LabelStyle(defaultFont, yellow ), Label.LabelStyle.class);
		skin.add("scoreStyle", new Label.LabelStyle(scoreFont, yellow), Label.LabelStyle.class);
		skin.add("buttonStyle", new TextButton.TextButtonStyle(null, null, null, defaultFont), TextButton.TextButtonStyle.class);
	}
	//</editor-fold>
}
