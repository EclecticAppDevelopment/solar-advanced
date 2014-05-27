package com.eclecticapps.soladvanced;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g3d.environment.*;
import java.util.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class solAdvanced implements ApplicationListener
{
//	final solAdvanced context = this;
	public float dT, camPos, angle;
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
	public Color dirLights;
	public float camFact, rotFact;
	public Vector3 touchPoint;
	
//	public Button preP, nexP;
	public SpriteBatch uiBatch;
	public int selP, i;
	public ModelInstance AstI;
	
//	public SpriteBatch sprite;
//	public BitmapFont font;
//	public Matrix4 viewMatrix;
	public Music bgMusic;

	public class Planet{
		public String name;
		public float x, y, z, r, d, o;
		public int div;
		public Color c;
		public Model m;
		public ModelInstance i;
	}
	public Planet Sol, Mercury, Venus, Earth, Moon, Mars, Asteroid, Jupiter, Saturn, SatRings, Uranus, Neptune, Pluto;
	public ArrayList<Planet> planets = new ArrayList<Planet>();
	public ArrayList<ModelInstance> asteroids = new ArrayList<ModelInstance>();
	
	@Override
	public void create() {

		
		// Environment for lights
		environment = new Environment();
		// Model batch to draw all stars/planets
		modelBatch = new ModelBatch();
		// Builds each object
    	modelBuilder = new ModelBuilder();
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("Eddies_Twister.mp3"));
		bgMusic.play();
		bgMusic.setLooping(true);

//		sprite = new SpriteBatch();
//		font = new BitmapFont();
//		viewMatrix = new Matrix4();
//		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		sprite.setProjectionMatrix(viewMatrix);

		dT = 0;
		angle = 0;
		camPos = 20000f;
		dirLights = new Color(Color.WHITE);
		camFact = 5000f;
		rotFact = 10;
		EnviroLights();
		CameraSet();
		cam.position.set(0, 0, camPos);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 150000f;
		cam.update();
    	touchPoint = new Vector3();
		
//		preP = new Button();
//		nexP = new Button();
		uiBatch = new SpriteBatch();
		selP = 0; /* Sol */
		
		//
		PlanetsCreate();
		planets.add(Sol);
		planets.add(Mercury);
		planets.add(Venus);
		planets.add(Earth);
		planets.add(Moon);
		planets.add(Mars);
	//	planets.add(Asteroid);
		planets.add(Jupiter);
		planets.add(Saturn);
		planets.add(SatRings);
		planets.add(Uranus);
		planets.add(Neptune);
		planets.add(Pluto);

		// Set control of camera to touch/pan/pinch
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}
	
	public void EnviroLights(){
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.2f, 0.2f, 1f));
//		environment.add(new PointLight().set(dirLights, -1f, 0, 0, 1f));
//		environment.add(new PointLight().set(dirLights, 1f, 0, 0, 1));
//		environment.add(new PointLight().set(dirLights, 0, -1f, 0, 1));
//		environment.add(new PointLight().set(dirLights, 0, 1f, 0, 1));
//		environment.add(new PointLight().set(dirLights, 0, 0, 1f, 1));
//		environment.add(new PointLight().set(dirLights, 0, 0, -1f, 1));
		environment.add(new DirectionalLight().set(dirLights, -1f, 0, 0));
		environment.add(new DirectionalLight().set(dirLights, 1f, 0, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, -1f, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, 1f, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, 0, 1f));
		environment.add(new DirectionalLight().set(dirLights, 0, 0, -1f));
	}

	public void CameraSet(){
		cam = new PerspectiveCamera(50, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private void PlanetsCreate()
	{
		Sol = new Planet();
		Sol.name = "Sol";
		Sol.x = 0;
		Sol.y = 0;
		Sol.z = 0;
		Sol.div = 80;
		Sol.c = new Color(Color.ORANGE.add(Color.YELLOW));
		Sol.r = 1391.1f;
		Sol.m = modelBuilder.createSphere(Sol.r, Sol.r, Sol.r, Sol.div, Sol.div,
										  new Material(ColorAttribute.createDiffuse(Sol.c)),
										  Usage.Position | Usage.Normal);
		Sol.i = new ModelInstance(Sol.m);
		Sol.i.transform.translate(Sol.x, Sol.y, Sol.z);
		//
		Mercury = new Planet();
		Mercury.name = "Mercury";
		Mercury.d = Sol.r + 580f;
		Mercury.r = 50f;
		Mercury.c = new Color(Color.LIGHT_GRAY);
		Mercury.x = Mercury.d;
		Mercury.y = 0;
		Mercury.z = 0;
		Mercury.o = (float) (88 / 365.25);
		Mercury.div = 40;
		Mercury.m = modelBuilder.createSphere(Mercury.r, Mercury.r, Mercury.r, Mercury.div, Mercury.div,
											  new Material(ColorAttribute.createDiffuse(Mercury.c)),
											  Usage.Position | Usage.Normal);
		Mercury.i = new ModelInstance(Mercury.m);
		Mercury.i.transform.translate(Mercury.x, Mercury.y, Mercury.z);
		//
		Venus = new Planet();
		Venus.d = Sol.r + 1080f;
		Venus.r = 120f;
		Venus.c = new Color(Color.MAGENTA);
		Venus.x = Venus.d;
		Venus.y = 0;
		Venus.z = 0;
		Venus.o = (float) (224.7 / 365.25);
		Venus.div = 40;
		Venus.m = modelBuilder.createSphere(Venus.r, Venus.r, Venus.r, Venus.div, Venus.div,
											new Material(ColorAttribute.createDiffuse(Venus.c)),
											Usage.Position | Usage.Normal);
		Venus.i = new ModelInstance(Venus.m);
		Venus.i.transform.translate(Venus.x, Venus.y, Venus.z);
		//
		Earth = new Planet();
		Earth.d = Sol.r + 1500f;
		Earth.r = 130f;
		Earth.c = new Color(Color.CYAN);
		Earth.x = 0;
		Earth.y = Earth.d;
		Earth.z = 0;
		Earth.o = (float) (365.25 / 365.25);
		Earth.div = 40;
		Earth.m = modelBuilder.createSphere(Earth.r, Earth.r, Earth.r, Earth.div, Earth.div,
											new Material(ColorAttribute.createDiffuse(Earth.c)),
											Usage.Position | Usage.Normal);
		Earth.i = new ModelInstance(Earth.m);
		Earth.i.transform.translate(Earth.x, Earth.y, Earth.z);
		//
		Moon = new Planet();
		Moon.d = Earth.r + 15f;
		Moon.r = 13f;
		Moon.c = new Color(Color.LIGHT_GRAY);
		Moon.x = Earth.x;
		Moon.y = Earth.y + Moon.d;
		Moon.z = Earth.z;
		Moon.o = (float) (28.5 / 365.25);
		Moon.div = 40;
		Moon.m = modelBuilder.createSphere(Moon.r, Moon.r, Moon.r, Moon.div, Moon.div,
										   new Material(ColorAttribute.createDiffuse(Moon.c)),
										   Usage.Position | Usage.Normal);
		Moon.i = new ModelInstance(Moon.m);
		Moon.i.transform.translate(Moon.x, Moon.y, Moon.z);
		//
		Mars = new Planet();
		Mars.d = Sol.r + 2280f;
		Mars.r = 70f;
		Mars.c = new Color(Color.RED);
		Mars.x = Mars.d;
		Mars.y = 0;
		Mars.z = 0;
		Mars.o = (float) (686.98 / 365.25);
		Mars.div = 40;
		Mars.m = modelBuilder.createSphere(Mars.r, Mars.r, Mars.r, Mars.div, Mars.div,
										   new Material(ColorAttribute.createDiffuse(Mars.c)),
										   Usage.Position | Usage.Normal);
		Mars.i = new ModelInstance(Mars.m);
		Mars.i.transform.translate(Mars.x, Mars.y, Mars.z);
		//
		Asteroid = new Planet();
		Asteroid.d = Sol.r + 3280f;
		Asteroid.r = 5f;
		Asteroid.c = new Color(Color.GRAY);
		Asteroid.x = 0;
		Asteroid.y = 0;
		Asteroid.z = 0;
		Asteroid.o = (float) (706.98 / 365.25);
		Asteroid.div = 20;
		Asteroid.m = modelBuilder.createSphere(Asteroid.r, Asteroid.r, Asteroid.r, Asteroid.div, Asteroid.div,
										   new Material(ColorAttribute.createDiffuse(Asteroid.c)),
										   Usage.Position | Usage.Normal);
//		for (int i = 0; i < 50; i++){
//		    AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()));
//			asteroids.add(AstI);
//			AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()));
//			asteroids.add(AstI);
//			AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()));
//			asteroids.add(AstI);
//			AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()));
//			asteroids.add(AstI);
//			AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()));
//			asteroids.add(AstI);
//			AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()));
//			asteroids.add(AstI);
//			AstI = new ModelInstance(Asteroid.m);
//			AstI.transform.translate(Asteroid.d + (float)(1000f * Math.random()), Asteroid.d + (float)(-1000f * Math.random()), Asteroid.d + (float)(1000f * Math.random()));
//			asteroids.add(AstI);
//		}
//		Asteroid.i = new ModelInstance(Asteroid.m);
//		Asteroid.i.transform.translate(Asteroid.x, Asteroid.y, Asteroid.z);
		//
		Jupiter = new Planet();
		Jupiter.d = Sol.r + 7780f;
		Jupiter.r = 1430f;
		Jupiter.c = new Color(Color.RED);
		Jupiter.x = 0;
		Jupiter.y = Jupiter.d;
		Jupiter.z = 0;
		Jupiter.o = (float) (11.86);
		Jupiter.div = 80;
		Jupiter.m = modelBuilder.createSphere(Jupiter.r, Jupiter.r, Jupiter.r, Jupiter.div, Jupiter.div,
											  new Material(ColorAttribute.createDiffuse(Jupiter.c)),
											  Usage.Position | Usage.Normal);
		Jupiter.i = new ModelInstance(Jupiter.m);
		Jupiter.i.transform.translate(Jupiter.x, Jupiter.y, Jupiter.z);
		//
		Saturn = new Planet();
		Saturn.d = Sol.r + 10800f;
		Saturn.r = 1200f;
		Saturn.c = new Color(Color.LIGHT_GRAY);
		Saturn.x = Saturn.d;
		Saturn.y = 0;
		Saturn.z = 0;
		Saturn.o = (float) (29.46);
		Saturn.div = 80;
		Saturn.m = modelBuilder.createSphere(Saturn.r, Saturn.r, Saturn.r, Saturn.div, Saturn.div,
											 new Material(ColorAttribute.createDiffuse(Saturn.c)),
											 Usage.Position | Usage.Normal);
		Saturn.i = new ModelInstance(Saturn.m);
		Saturn.i.transform.translate(Saturn.x, Saturn.y, Saturn.z);
		//
		SatRings = new Planet();
		SatRings.d = Sol.r + 10800f;
		SatRings.r = 2200f;
		SatRings.c = new Color(Color.GRAY);
		SatRings.x = SatRings.d;
		SatRings.y = 0;
		SatRings.z = 0;
		SatRings.o = (float) (29.46);
		SatRings.div = 80;
		SatRings.m = modelBuilder.createSphere(SatRings.r, SatRings.r, 2, SatRings.div, SatRings.div,
											   new Material(ColorAttribute.createDiffuse(SatRings.c)),
											   Usage.Position | Usage.Normal);
		SatRings.i = new ModelInstance(SatRings.m);
		SatRings.i.transform.translate(SatRings.x, SatRings.y, SatRings.z);
		//		
		Uranus = new Planet();
		Uranus.d = Sol.r + 15800f;
		Uranus.r = 400f;
		Uranus.c = new Color(Color.GREEN);
		Uranus.x = Uranus.d;
		Uranus.y = 0;
		Uranus.z = 0;
		Uranus.o = (float) (164.79);
		Uranus.div = 40;
		Uranus.m = modelBuilder.createSphere(Uranus.r, Uranus.r, Uranus.r, Uranus.div, Uranus.div,
											 new Material(ColorAttribute.createDiffuse(Uranus.c)),
											 Usage.Position | Usage.Normal);
		Uranus.i = new ModelInstance(Uranus.m);
		Uranus.i.transform.translate(Uranus.x, Uranus.y, Uranus.z);
		//
		Neptune = new Planet();
		Neptune.d = Sol.r + 25800f;
		Neptune.r = 700f;
		Neptune.c = new Color(Color.BLUE);
		Neptune.x = 0;
		Neptune.y = Neptune.d;
		Neptune.z = 0;
		Neptune.o = (float) (84.0);
		Neptune.div = 40;
		Neptune.m = modelBuilder.createSphere(Neptune.r, Neptune.r, Neptune.r, Neptune.div, Neptune.div,
											  new Material(ColorAttribute.createDiffuse(Neptune.c)),
											  Usage.Position | Usage.Normal);
		Neptune.i = new ModelInstance(Neptune.m);
		Neptune.i.transform.translate(Neptune.x, Neptune.y, Neptune.z);
		//
		Pluto = new Planet();
		Pluto.d = Sol.r + 38800f;
		Pluto.r = 30f;
		Pluto.c = new Color(Color.LIGHT_GRAY);
		Pluto.x = 0;
		Pluto.y = Pluto.d;
		Pluto.z = 0;
		Pluto.o = (float) (248.54);
		Pluto.div = 40;
		Pluto.m = modelBuilder.createSphere(Pluto.r, Pluto.r, Pluto.r, Pluto.div, Pluto.div,
											new Material(ColorAttribute.createDiffuse(Pluto.c)),
											Usage.Position | Usage.Normal);
		Pluto.i = new ModelInstance(Pluto.m);
		Pluto.i.transform.translate(Pluto.x, Pluto.y, Pluto.z);
		//
	}

	@Override
	public void render() {
		
		dT = Gdx.graphics.getDeltaTime();
		camController.pinchZoomFactor = camFact;
		camController.alwaysScroll = true;
	//	camController.setMaxFlingDelay(20);
		camController.update();
		uiDraw();
		uiTouch();
	//	BodyTouch();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		DrawBodies();
		angle = angle + (1 / rotFact);
//			cam.rotateAround(Vector3.Zero, new Vector3(0,0,1), 2f);
//			cam.update();
////	        	// Need to apply to all objects except Sol
//        		for (DirectionalLight light: environment.directionalLights){
//        			light.set(0.8f, 0.8f, 0.8f, (float)Math.sin(dT), (float)Math.cos(dT), -0.2f);
//        		}
        	
	    angle = (float)Math.toRadians(angle);
		for (Planet a : planets){
			if (a == Moon){
				float newX = (float)Math.cos((1 / a.o) * angle) * (a.x - Earth.x) - (float)Math.sin((1 / a.o) * angle) * (a.y - Earth.y) + Earth.x;
				float newY = (float)Math.sin((1 / a.o) * angle) * (a.x - Earth.x) + (float)Math.cos((1 / a.o) * angle) * (a.y - Earth.y) + Earth.y;
				a.i = null;
				a.i = new ModelInstance(a.m);
				a.i.transform.translate(newX, newY, 0);
		        a.x = newX;
        		a.y = newY;
			}else if (a != Sol){
				float newX = (float)Math.cos((1 / a.o) * angle) * (a.x - Sol.x) - (float)Math.sin((1 / a.o) * angle) * (a.y - Sol.y) + Sol.x;
				float newY = (float)Math.sin((1 / a.o) * angle) * (a.x - Sol.x) + (float)Math.cos((1 / a.o) * angle) * (a.y - Sol.y) + Sol.y;
				a.i = null;
				a.i = new ModelInstance(a.m);
				a.i.transform.translate(newX, newY, 0);
				// Moon must ALSO translate based on Earth
				if (a == Earth){
					Moon.x += newX - a.x;
					Moon.y += newY - a.y;
					Moon.i = null;
					Moon.i = new ModelInstance(Moon.m);
					Moon.i.transform.translate(Moon.x, Moon.y, 0);
				}
				a.x = newX;
        		a.y = newY;
			}else{

			}
		}
	}

	private void uiTouch()
	{
	//	uiBatch.begin();
	//	uiBatch.draw(preP, 0, 0, 100, 100);
		// TODO: Implement this method
	}

	private void uiDraw()
	{
		// TODO: Implement this method
	}

	public void BodyTouch()
	{
		touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	//	cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		if (Gdx.input.isTouched()) {
		//	Toast.makeText(context, "Button is clicked", Toast.LENGTH_LONG).show();
//			for (Planet a : planets) {
////				Vector3 V = new Vector3(a.x, a.y, a.r);
//				if (touchDown(a, touchPoint.x, touchPoint.y)){
//					cam.lookAt(a.x, a.y, a.z);
//					cam.update();
////					a.c = Color.WHITE;
////					a.i = null;
////					a.i = new ModelInstance(a.m);
//				}
////				cam.unproject(V);
////				if (pointInPlanet(a, V, touchPoint.x, touchPoint.y)) {
////					a.c = Color.WHITE;
////					a.i = null;
////					a.i = new ModelInstance(a.m);
//				}
//		    }
    	}
	}
	
//	@Override
//	public void toast(String text, boolean isLong) {
//		toast(text, isLong, this);
//	}

//	public void toast(final String text, final boolean isLong,
//					  final solAdvanced context) {
//	//	handler.post(new Runnable() {
////				@Override
//				public void run() {
//					final Toast tst = new Toast(context);
//					LinearLayout toastLayout = new LinearLayout(context);
//					toastLayout.setBackgroundColor(Color.toIntBits(20, 20, 20, 255));
//					toastLayout.setPadding(10, 10, 10, 10);
//					TextView toastView = new TextView(context);
//					toastView.setText(text);
//					toastView.setTextSize(24);
//					toastView.setGravity(Gravity.CENTER);
//					toastLayout.addView(toastView);
//					tst.setView(toastLayout);
//					tst.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
//					tst.show();
//				}
//			});
//	}

	public static boolean pointInPlanet (Planet p, Vector3 v, float x, float y) {
		return v.x <= x && v.x + p.r >= x && v.y <= y && v.y + p.r >= y;
	}

	public void DrawBodies(){
		modelBatch.begin(cam);
		for (Planet a : planets){
			modelBatch.render(a.i, environment);}
		for (ModelInstance a : asteroids){
			modelBatch.render(a, environment);}
		modelBatch.end();
	}

	@Override
    public boolean touchDown (Planet p, float x, float y) {
          Vector3 touchDown = new Vector3(x, y, 0);
          cam.unproject(touchDown);
          Vector3 obPos = new Vector3(p.x, p.y, p.z);
          obPos.sub(touchDown);
    return true;
    }
   
	@Override
	public void dispose() {
		modelBatch.dispose();
		planets.clear();
		for (Planet a : planets){
		    a.m.dispose();
		}
		bgMusic.dispose();
	}

	@Override
	public void resize(int width, int height) {
		//	CameraSet();
		//cam = new PerspectiveCamera(50, cam.viewportHeight * (width / height), cam.viewportHeight);
//		float aspectRatio = (float) width / (float) height;
//		cam = new PerspectiveCamera(50, cam.viewportHeight * aspectRatio, cam.viewportHeight);
////		viewMatrix = new Matrix4();
//		viewMatrix.setToOrtho2D(0, 0, width, height);
//		sprite.setProjectionMatrix(viewMatrix);

	}

	@Override
	public void pause() {
		bgMusic.pause();
	}

	@Override
	public void resume() {
		bgMusic.play();
	}
}
