package com.eclecticapps.soladvanced;

// @Author Matthew Tiernan
// @From Eclectic App Development
// @Web http://eclecticappdevelopment.co.uk
// @Date May 2014
// @License: Creative Commons Attribution-Non-Commercial
// @LicenseLink: https://creativecommons.org/licenses/by-nc/3.0/

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
import android.graphics.*;

public class solAdvanced implements ApplicationListener
{
	public float dT, camPos, angle;
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
	public Color dirLights;
	public float camFact, rotFact;
	public Vector3 touchPoint;
	public float AstDmin, AstDmax, fontX, fontY;
	public int selP;
	public ModelInstance AstI;
	public Music bgMusic;

	public class Planet{
		public String name;
		public float x, y, z, r, d, o;
		public int div;
		public Color c;
		public Model m;
		public ModelInstance i;
	}
	public class Ast{
		public float x, y, z, d;
		public ModelInstance i;
	}
	public Planet Sol, Mercury, Venus, Earth, Moon, Mars, Asteroid, Jupiter, Saturn, SatRings, Uranus, Neptune, Pluto;
	public ArrayList<Planet> planets = new ArrayList<Planet>();
	public Ast asteroid;
	public ArrayList<ModelInstance> asteroids = new ArrayList<ModelInstance>();
	public int astCount;
	
	public SpriteBatch spriteBatch;
	public BitmapFont font;
	CharSequence str, strN, strD, strR, strO, strX, strY, strZ;
	
	@Override
	public void create() {

		strN = "Focus: Sol"  + "\n" ;
		strD = "Mean Orbital Distance: 0 km"  + "\n" ;
		strR = "Radius: 1391000 km"  + "\n" ;
		strO = "Orbit Time: 0 days"  + "\n" ;
		strX = "X-Position: 0"  + "\n" ;
		strY = "Y-Position: 0"  + "\n" ;
		strZ = "Z-Position: 0"  + "\n" ;
		str = strN + "" + strD  + "" + strR  + "" + strO + "" + strX + "" + strY  + "" + strZ;
		
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
		fontX = Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 5);
		fontY = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 25);
		
		// Multiples of 4 only - keep under 100 asteroids
		astCount = 64;
		// Environment for lights
		environment = new Environment();
		// Model batch to draw all stars/planets
		modelBatch = new ModelBatch();
		// Builds each object
    	modelBuilder = new ModelBuilder();
		
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("Eddies_Twister.mp3"));
		bgMusic.play();
		bgMusic.setLooping(true);
		dT = 0;
		angle = 0;
		camPos = 5000f;
		dirLights = new Color(Color.WHITE);
		camFact = 5000f;
		rotFact = 10;
		EnviroLights();
		CameraSet();
		cam.position.set(0, camPos, camPos);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 500000f;
		cam.update();
    	touchPoint = new Vector3();
		selP = 0; /* Sol */
		
		//
		PlanetsCreate();
		planets.add(Sol);
		planets.add(Mercury);
		planets.add(Venus);
		planets.add(Earth);
		planets.add(Moon);
		planets.add(Mars);
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
		Sol.o = 0;
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
		Mercury.r = 48.8f;
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
		Venus.name = "Venus";
		Venus.d = Sol.r + 1080f;
		Venus.r = 121f;
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
		Earth.name = "Earth";
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
		Moon.name = "Earth's Moon";
		Moon.d = Earth.r + 10f;
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
		Mars.name = "Mars";
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
		Jupiter = new Planet();
		Jupiter.name = "Jupiter";
		Jupiter.d = Sol.r + 7780f;
		Jupiter.r = 1430f;
		Jupiter.c = new Color(1, 0.3f, 0.3f, 0.3f);
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
		Asteroid = new Planet();
		Asteroid.name = "Asteroid";
		Asteroid.r = 5f;
		Asteroid.c = new Color(Color.GRAY);
		Asteroid.div = 20;
		Asteroid.m = modelBuilder.createSphere(Asteroid.r, Asteroid.r, Asteroid.r, Asteroid.div, Asteroid.div,
										   new Material(ColorAttribute.createDiffuse(Asteroid.c)),
										   Usage.Position | Usage.Normal);
		AstDmin = Mars.d + 0.2f * (Jupiter.d - Mars.d);
		AstDmin = Jupiter.d - 0.2f * (Jupiter.d - Mars.d);

		for (int a = 0; a < astCount / 4; a++)
		{
			// +ve X, take +ve root for Y
	    	asteroid = new Ast();
		    asteroid.i = new ModelInstance(Asteroid.m);
			asteroid.d = (float)(Math.random()*AstDmax) + AstDmin;
			asteroid.x = (float)(Math.random()*asteroid.d);
			asteroid.y = (float)Math.abs(Math.sqrt((asteroid.d * asteroid.d) - (asteroid.x * asteroid.x)));
			asteroid.z = (float)(40* Math.random()) - 20;
			asteroid.i.transform.translate(asteroid.x, asteroid.y, asteroid.z);
			asteroids.add(asteroid.i);
			// +ve X, take -ve root for Y
	    	asteroid = new Ast();
		    asteroid.i = new ModelInstance(Asteroid.m);
			asteroid.d = (float)(Math.random()*AstDmax) + AstDmin;
			asteroid.x = (float)(Math.random()*asteroid.d);
			asteroid.y = (float)-Math.abs(Math.sqrt((asteroid.d * asteroid.d) - (asteroid.x * asteroid.x)));
			asteroid.z = (float)(40* Math.random()) - 20;
			asteroid.i.transform.translate(asteroid.x, asteroid.y, asteroid.z);
			asteroids.add(asteroid.i);
			// -ve X, take +ve root for Y
	    	asteroid = new Ast();
		    asteroid.i = new ModelInstance(Asteroid.m);
			asteroid.d = (float)(Math.random()*AstDmax) + AstDmin;
			asteroid.x = (float)-(Math.random()*asteroid.d);
			asteroid.y = (float)Math.abs(Math.sqrt((asteroid.d * asteroid.d) - (asteroid.x * asteroid.x)));
			asteroid.z = (float)(40* Math.random()) - 20;
			asteroid.i.transform.translate(asteroid.x, asteroid.y, asteroid.z);
			asteroids.add(asteroid.i);
			// -ve X, take -ve root for Y
	    	asteroid = new Ast();
		    asteroid.i = new ModelInstance(Asteroid.m);
			asteroid.d = (float)(Math.random()*AstDmax) + AstDmin;
			asteroid.x = (float)-(Math.random()*asteroid.d);
			asteroid.y = (float)-Math.abs(Math.sqrt((asteroid.d * asteroid.d) - (asteroid.x * asteroid.x)));
			asteroid.z = (float)(40* Math.random()) - 20;
			asteroid.i.transform.translate(asteroid.x, asteroid.y, asteroid.z);
			asteroids.add(asteroid.i);
		}
		//
		Saturn = new Planet();
		Saturn.name = "Saturn";
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
		SatRings.name = "Saturn's Rings";
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
		Uranus.name = "Uranus";
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
		Neptune.name = "Neptune";
		Neptune.d = Sol.r + 45043f;
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
		Pluto.name = "Pluto";
		Pluto.d = Sol.r + 30000f;
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
		camController.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.lookAt(planets.get(selP).x, planets.get(selP).y, planets.get(selP).z);
		cam.update();
		NextPlanet();
		DrawBodies();
		Orbit();
		strX = "X-Position: " + planets.get(selP).x + "\n";
		strY = "Y-Position: " + planets.get(selP).y + "\n";
		strZ = "Z-Position: " + planets.get(selP).z + "\n";
		str = strN +""+ strD  +""+ strR +""+ strO +""+strX +""+ strY +""+strZ;

		spriteBatch.begin();
		//font.draw(spriteBatch, str, fontX, fontY);
		font.drawMultiLine(spriteBatch, str, fontX, fontY);
		spriteBatch.end();
		
	//RenderEnd   
	}

	private void NextPlanet()
	{
		if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1) && Gdx.input.isTouched(2) && Gdx.input.justTouched()){
			strN = "Focus: ";
			strD = "Mean Orbital Distance: ";
			strR = "Radius: ";
			strO = "Orbit Time: ";
			strX = "X-Position: ";
			strY = "Y-Position: ";
			strZ = "Z-Position: ";
			
			if (planets.get(selP) == Pluto){
				selP = 0;
			}else if(planets.get(selP) == Saturn){
				selP = selP + 2;
			}
			else{
				selP = selP + 1;
			}
			strN += planets.get(selP).name + "\n";
			strD += Math.round( planets.get(selP).d )+ "00000 km" + "\n";
			strR += 100 * planets.get(selP).r + " km" + "\n";
			strO += Math.round( 365.25 * planets.get(selP).o )+ " days" + "\n";
			strX += planets.get(selP).x + "\n";
			strY += planets.get(selP).y + "\n";
			strZ += planets.get(selP).z + "\n";
			str = strN +""+ strD  +""+ strR +""+ strO +""+strX +""+ strY +""+strZ;
			font.setColor(planets.get(selP).c);
			cam.position.set(planets.get(selP).x, planets.get(selP).y + camPos, planets.get(selP).z + camPos);
		}
	// NextPlanetEnd
	}

	private void Orbit()
	{
		angle = angle + (1 / rotFact);
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
			}else{}
		}
	//OrbitEnd
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
	public void dispose() {
		modelBatch.dispose();
		planets.clear();
		for (Planet a : planets){
		    a.m.dispose();}
		
		bgMusic.dispose();
	}

	@Override
	public void resize(int width, int height) {
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
