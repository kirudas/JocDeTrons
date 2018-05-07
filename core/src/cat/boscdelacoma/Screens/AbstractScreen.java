package cat.boscdelacoma.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import cat.boscdelacoma.JocDeTrons;

public class AbstractScreen implements Screen, InputProcessor {
    protected final JocDeTrons joc;
    protected final SpriteBatch batch;
    protected final Stage stage;
    private FitViewport viewport;
    private Texture imatge;

    public AbstractScreen(JocDeTrons joc) {
        this.joc = joc;
        this.batch = new SpriteBatch();
        this.stage = new Stage();

        Gdx.input.setInputProcessor(this);
        this.viewport = new FitViewport((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        Pixmap pixels = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixels.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pixels.fill();
        this.imatge = new Texture(pixels);
    }

    public void render(float delta) {
        this.stage.act(delta);
        this.stage.draw();
    }

    public void calculRedimensionat() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
        this.batch.getProjectionMatrix().idt().setToOrtho2D(0.0F, 0.0F, (float)screenWidth, (float)screenHeight);
        this.batch.getTransformMatrix().idt();
        this.batch.begin();
        float zonaEsquerra = (float)this.viewport.getLeftGutterWidth();
        if(zonaEsquerra > 0.0F) {
            this.batch.draw(this.imatge, 0.0F, 0.0F, zonaEsquerra, (float)screenHeight);
            this.batch.draw(this.imatge, (float)this.viewport.getRightGutterX(), 0.0F, (float)this.viewport.getRightGutterWidth(), (float)screenHeight);
        }

        float zonaInferior = (float)this.viewport.getBottomGutterHeight();
        if(zonaInferior > 0.0F) {
            this.batch.draw(this.imatge, 0.0F, 0.0F, (float)screenWidth, zonaInferior);
            this.batch.draw(this.imatge, 0.0F, (float)this.viewport.getTopGutterY(), (float)screenWidth, (float)this.viewport.getTopGutterHeight());
        }

        this.batch.end();
        this.viewport.update(screenWidth, screenHeight, true);
    }

    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    public void show() {
    }

    public void hide() {
        this.dispose();
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
        Gdx.app.log(JocDeTrons.class.getSimpleName(), "Alliberant recursos de la pantalla: " + this.getName());
        this.stage.dispose();
        this.batch.dispose();
        this.imatge.dispose();
    }

    protected String getName() {
        return this.getClass().getSimpleName();
    }

    protected JocDeTrons getGame() {
        return this.joc;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}
