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

public class solAdvanced implements ApplicationListener
{
	float dT, camPos;

	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Color dirLights;
	public float camFact;
	public Vector3 touchPoint;

	public class Planet{
		public float x, y, z, r, d;
		public int div;
		public Color c;
		public Model m;
		public ModelInstance i;
	}
	public Planet Sol, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto;


	@Override
	public void create() {

		// Environment for lights
		environment = new Environment();
		// Model batch to draw all stars/planets
		modelBatch = new ModelBatch();
		// Builds each object
		ModelBuilder modelBuilder = new ModelBuilder();

		dT = 0;
		camPos = 2000f;
		dirLights = new Color(Color.WHITE);
		camFact = 250f;
		EnviroLights();
		CameraSet();
    	touchPoint = new Vector3();
		//
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
		Mercury.r = 5f;
		Mercury.c = new Color(Color.LIGHT_GRAY);
		Mercury.x = Mercury.d;
		Mercury.y = 0;
		Mercury.z = 0;
		Mercury.div = 40;
		Mercury.m = modelBuilder.createSphere(Mercury.r, Mercury.r, Mercury.r, Mercury.div, Mercury.div,
											  new Material(ColorAttribute.createDiffuse(Mercury.c)),
											  Usage.Position | Usage.Normal);
		Mercury.i = new ModelInstance(Mercury.m);
		Mercury.i.transform.translate(Mercury.x, Mercury.y, Mercury.z);
		//
		Venus = new Planet();
		Venus.d = Sol.r + 1080f;
		Venus.r = 12f;
		Venus.c = new Color(Color.MAGENTA);
		Venus.x = 0;
		Venus.y = Venus.d;
		Venus.z = 0;
		Venus.div = 40;
		Venus.m = modelBuilder.createSphere(Venus.r, Venus.r, Venus.r, Venus.div, Venus.div,
											  new Material(ColorAttribute.createDiffuse(Venus.c)),
											  Usage.Position | Usage.Normal);
		Venus.i = new ModelInstance(Venus.m);
		Venus.i.transform.translate(Venus.x, Venus.y, Venus.z);
		//
		Earth = new Planet();
		Earth.d = Sol.r + 1500f;
		Earth.r = 13f;
		Earth.c = new Color(Color.CYAN);
		Earth.x = 0;
		Earth.y = 0;
		Earth.z = Earth.d;
		Earth.div = 40;
		Earth.m = modelBuilder.createSphere(Earth.r, Earth.r, Earth.r, Earth.div, Earth.div,
											new Material(ColorAttribute.createDiffuse(Earth.c)),
											Usage.Position | Usage.Normal);
		Earth.i = new ModelInstance(Earth.m);
		Earth.i.transform.translate(Earth.x, Earth.y, Earth.z);
		//
		Mars = new Planet();
		Mars.d = Sol.r + 2280f;
		Mars.r = 7f;
		Mars.c = new Color(Color.RED);
		Mars.x = Mars.d;
		Mars.y = 0;
		Mars.z = Mars.d;
		Mars.div = 40;
		Mars.m = modelBuilder.createSphere(Mars.r, Mars.r, Mars.r, Mars.div, Mars.div,
										   new Material(ColorAttribute.createDiffuse(Mars.c)),
											Usage.Position | Usage.Normal);
		Mars.i = new ModelInstance(Mars.m);
		Mars.i.transform.translate(Mars.x, Mars.y, Mars.z);
		//
		Jupiter = new Planet();
		Jupiter.d = Sol.r + 7780f;
		Jupiter.r = 143f;
		Jupiter.c = new Color(Color.GREEN.add(Color.BLUE));
		Jupiter.x = Jupiter.d;
		Jupiter.y = Jupiter.d;
		Jupiter.z = 0;
		Jupiter.div = 40;
		Jupiter.m = modelBuilder.createSphere(Jupiter.r, Jupiter.r, Jupiter.r, Jupiter.div, Jupiter.div,
											  new Material(ColorAttribute.createDiffuse(Jupiter.c)),
										   Usage.Position | Usage.Normal);
		Jupiter.i = new ModelInstance(Jupiter.m);
		Jupiter.i.transform.translate(Jupiter.x, Jupiter.y, Jupiter.z);
		//
		Saturn = new Planet();
		Saturn.d = Sol.r + 10800f;
		Saturn.r = 120f;
		Saturn.c = new Color(Color.YELLOW.add(Color.BLUE));
		Saturn.x = Saturn.d;
		Saturn.y = Saturn.d;
		Saturn.z = Saturn.d;
		Saturn.div = 40;
		Saturn.m = modelBuilder.createSphere(Saturn.r, Saturn.r, Saturn.r, Saturn.div, Saturn.div,
											 new Material(ColorAttribute.createDiffuse(Saturn.c)),
											  Usage.Position | Usage.Normal);
		Saturn.i = new ModelInstance(Saturn.m);
		Saturn.i.transform.translate(Saturn.x, Saturn.y, Saturn.z);
		//
		Uranus = new Planet();
		Uranus.d = Sol.r + 15800f;
		Uranus.r = 40f;
		Uranus.c = new Color(Color.BLUE);
		Uranus.x = 0;
		Uranus.y = Uranus.d;
		Uranus.z = Uranus.d;
		Uranus.div = 40;
		Uranus.m = modelBuilder.createSphere(Uranus.r, Uranus.r, Uranus.r, Uranus.div, Uranus.div,
											 new Material(ColorAttribute.createDiffuse(Uranus.c)),
											 Usage.Position | Usage.Normal);
		Uranus.i = new ModelInstance(Uranus.m);
		Uranus.i.transform.translate(Uranus.x, Uranus.y, Uranus.z);
		//
		Neptune = new Planet();
		Neptune.d = Sol.r + 25800f;
		Neptune.r = 70f;
		Neptune.c = new Color(Color.GREEN);
		Neptune.x = 0;
		Neptune.y = 0;
		Neptune.z = Neptune.d;
		Neptune.div = 40;
		Neptune.m = modelBuilder.createSphere(Neptune.r, Neptune.r, Neptune.r, Neptune.div, Neptune.div,
											  new Material(ColorAttribute.createDiffuse(Neptune.c)),
											 Usage.Position | Usage.Normal);
		Neptune.i = new ModelInstance(Neptune.m);
		Neptune.i.transform.translate(Neptune.x, Neptune.y, Neptune.z);
		//
		Pluto = new Planet();
		Pluto.d = Sol.r + 78800f;
		Pluto.r = 3f;
		Pluto.c = new Color(Color.GRAY);
		Pluto.x = Pluto.d;
		Pluto.y = Pluto.d;
		Pluto.z = Pluto.d;
		Pluto.div = 40;
		Pluto.m = modelBuilder.createSphere(Pluto.r, Pluto.r, Pluto.r, Pluto.div, Pluto.div,
											new Material(ColorAttribute.createDiffuse(Pluto.c)),
											 Usage.Position | Usage.Normal);
		Pluto.i = new ModelInstance(Pluto.m);
		Pluto.i.transform.translate(Pluto.x, Pluto.y, Pluto.z);
		//

		// Set control of camera to touch/pan/pinch
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
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
		cam.position.set(camPos, camPos, camPos);
		cam.lookAt(0,0,0);
		cam.near = 10f;
		cam.far = 15000f;
		cam.update();
	}

	@Override
	public void render() {
		dT = Gdx.graphics.getDeltaTime();
		camController.pinchZoomFactor = camFact;
		camController.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		DrawBodies();
//		TimeElapse();

		//   for (DirectionalLight light: environment.directionalLights){
		//			light.set(0.8f, 0.8f, 0.8f, (float)Math.sin(dT), (float)Math.cos(dT), -0.2f);
		//		}
	}

	public void TimeElapse()
	{
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		if (Gdx.input.justTouched()) {
			if (pointInPlanet(Sol, touchPoint.x, touchPoint.y)) {
				dirLights = Color.GREEN;
				EnviroLights();
			}
//			if (pointInPlanet(Earth, touchPoint.x, touchPoint.y)) {
//				dirLights = Color.RED;
//				EnviroLights();
//				}
//				
//			dirLights = Color.WHITE;
//			EnviroLights();
		}
	}

	public static boolean pointInPlanet (Planet p, float x, float y) {
		return p.x <= x && p.x + p.r >= x && p.y <= y && p.y + p.r >= y;
	}
	
	public void DrawBodies(){
		modelBatch.begin(cam);
		modelBatch.render(Sol.i, environment);
		modelBatch.render(Mercury.i, environment);
		modelBatch.render(Venus.i, environment);
		modelBatch.render(Earth.i, environment);
		modelBatch.render(Mars.i, environment);
		modelBatch.render(Jupiter.i, environment);
		modelBatch.render(Saturn.i, environment);
		modelBatch.render(Neptune.i, environment);
		modelBatch.render(Uranus.i, environment);
		modelBatch.render(Pluto.i, environment);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		Sol.m.dispose();
		Mercury.m.dispose();
		Venus.m.dispose();
		Earth.m.dispose();
		Mars.m.dispose();
		Jupiter.m.dispose();
		Saturn.m.dispose();
		Uranus.m.dispose();
		Neptune.m.dispose();
		Pluto.m.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
