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
import android.renderscript.*;
import java.util.*;

public class solAdvanced implements ApplicationListener
{
	public float dT, camPos, angle;
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
//	public SpriteBatch sprite;
//	public BitmapFont font;
	public Color dirLights;
	public float camFact, rotFact;
	public Vector3 touchPoint;
//	public Matrix4 viewMatrix;

	public class Planet{
		public float x, y, z, r, d, o;
		public int div;
		public Color c;
		public Model m;
		public ModelInstance i;
	}
	public Planet Sol, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto;
	public ArrayList<Planet> planets = new ArrayList<Planet>();

	@Override
	public void create() {

		// Environment for lights
		environment = new Environment();
		// Model batch to draw all stars/planets
		modelBatch = new ModelBatch();
		// Builds each object
    	modelBuilder = new ModelBuilder();
//		sprite = new SpriteBatch();
//		font = new BitmapFont();
//		viewMatrix = new Matrix4();
//		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		sprite.setProjectionMatrix(viewMatrix);

		dT = 0;
		angle = 0;
		camPos = 10000f;
		dirLights = new Color(Color.WHITE);
		camFact = 500f;
		rotFact = 10;
		EnviroLights();
		CameraSet();
		cam.position.set(camPos, camPos, camPos);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 50000f;
		cam.update();
    	touchPoint = new Vector3();
		//
		PlanetsCreate();
		planets.add(Sol);
		planets.add(Mercury);
		planets.add(Venus);
		planets.add(Earth);
		planets.add(Mars);
		planets.add(Jupiter);
		planets.add(Saturn);
		planets.add(Uranus);
		planets.add(Neptune);
		planets.add(Pluto);

//		for (Planet a: planets){
//			for (Planet b : planets){
//			//	if a.y
//			}
//		}

		// Set control of camera to touch/pan/pinch
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	private void PlanetsCreate()
	{
		Sol = new Planet();
		Sol.d = 0;
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
		Venus.y = Venus.d;
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
		Mars = new Planet();
		Mars.d = Sol.r + 2280f;
		Mars.r = 70f;
		Mars.c = new Color(Color.RED);
		Mars.x = Mars.d;
		Mars.y = -Mars.d;
		Mars.z = 0;
		Mars.o = (float) (686.98 / 365.25);
		Mars.div = 40;
		Mars.m = modelBuilder.createSphere(Mars.r, Mars.r, Mars.r, Mars.div, Mars.div,
										   new Material(ColorAttribute.createDiffuse(Mars.c)),
										   Usage.Position | Usage.Normal);
		Mars.i = new ModelInstance(Mars.m);
		Mars.i.transform.translate(Mars.x, Mars.y, Mars.z);
		//
		Jupiter = new Planet();
		Jupiter.d = Sol.r + 7780f;
		Jupiter.r = 1430f;
		Jupiter.c = new Color(Color.RED);
		Jupiter.x = 0;
		Jupiter.y = -Jupiter.d;
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
		Saturn.x = -Saturn.d;
		Saturn.y = -Saturn.d;
		Saturn.z = 0;
		Saturn.o = (float) (29.46);
		Saturn.div = 80;
		Saturn.m = modelBuilder.createSphere(Saturn.r, Saturn.r, Saturn.r, Saturn.div, Saturn.div,
											 new Material(ColorAttribute.createDiffuse(Saturn.c)),
											 Usage.Position | Usage.Normal);
		Saturn.i = new ModelInstance(Saturn.m);
		Saturn.i.transform.translate(Saturn.x, Saturn.y, Saturn.z);
		//
		Uranus = new Planet();
		Uranus.d = Sol.r + 15800f;
		Uranus.r = 400f;
		Uranus.c = new Color(Color.GREEN);
		Uranus.x = -Uranus.d;
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
		Neptune.x = -Neptune.d;
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

	public void EnviroLights(){
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.2f, 0.2f, 1f));
		environment.clear();
		environment.add(new DirectionalLight().set(dirLights, -1f, 0, 0));
		environment.add(new DirectionalLight().set(dirLights, 1f, 0, 0));
		environment.add(new DirectionalLight().set(dirLights , 0, -1f, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, 1f, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, 0, 1f));
		environment.add(new DirectionalLight().set(dirLights, 0, 0, -1f));
	}

	public void CameraSet(){
		cam = new PerspectiveCamera(50, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render() {
		//	dT = Gdx.graphics.getDeltaTime();
		camController.pinchZoomFactor = camFact;
		camController.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		DrawBodies();
		angle = angle + (1 / rotFact);
	//	cam.rotateAround(Vector3.Zero, new Vector3(0,0,1), 2f);
	//	cam.update();
	    angle = (float)Math.toRadians(angle); // Convert to radians
		for (Planet a : planets){
			if (a.r != Sol.r){
				float newX = (float)Math.cos((1 / a.o) * angle) * (a.x - Sol.x) - (float)Math.sin((1 / a.o) * angle) * (a.y - Sol.y) + Sol.x;
				float newY = (float)Math.sin((1 / a.o) * angle) * (a.x - Sol.x) + (float)Math.cos((1 / a.o) * angle) * (a.y - Sol.y) + Sol.y;
				a.i = null;
				a.i = new ModelInstance(a.m);
				a.i.transform.translate(newX, newY, 0);
		        a.x = newX;
        		a.y = newY;
			}else{

			}
		}

//		BodyTouch();
		// Need to apply to all objects except Sol
//		for (DirectionalLight light: environment.directionalLights){
//			light.set(0.8f, 0.8f, 0.8f, (float)Math.sin(dT), (float)Math.cos(dT), -0.2f);
//		}
//	}
//
//	public void BodyTouch()
//	{
//		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
//
//		if (Gdx.input.justTouched()) {
//			if (pointInPlanet(Sol, touchPoint.x, touchPoint.y)) {
//				dirLights = Color.GREEN;
//				EnviroLights();
//			}
////			if (pointInPlanet(Earth, touchPoint.x, touchPoint.y)) {
//				dirLights = Color.RED;
//				EnviroLights();
//				}
//				
//			dirLights = Color.WHITE;
//			EnviroLights();
//		}
	}

	public static boolean pointInPlanet (Planet p, float x, float y) {
		return p.x <= x && p.x + p.r >= x && p.y <= y && p.y + p.r >= y;
	}

	public void DrawBodies(){
		modelBatch.begin(cam);
		for (Planet a : planets){
			modelBatch.render(a.i, environment);}
		modelBatch.end();


	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		planets.clear();
		for (Planet a : planets){
		    a.m.dispose();
		}
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
	}

	@Override
	public void resume() {
	}
}
