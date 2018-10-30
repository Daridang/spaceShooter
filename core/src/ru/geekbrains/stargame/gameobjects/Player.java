package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import ru.geekbrains.stargame.StarGame;
import ru.geekbrains.stargame.animations.PlayerAnimation;

import static com.badlogic.gdx.Gdx.app;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;


public class Player {
    public static final float WIDTH = 80f;
    public static final float HEIGHT = 80f;
    private PlayerAnimation animation;
    private TextureRegion region;
    private Vector2 position;
    private Vector2 respawnPosition;
    private Vector2 velocity;
    private float lowEnginePower = 50f;
    private float enginePower = 200f;
    private float maxEnginePower = 300f;
    private float rotationSpeed = 200f;
    private Vector2 targetPosition;
    private boolean isTargetSet = false;
    private boolean isAlive = true;
    private float angle = 0f;

    private int lives = 3;

    private Rectangle hitBox;
    private Circle hitCircle;

    public Player(TextureAtlas atlas) {
        animation = new PlayerAnimation(atlas);
        region = animation.getIdle().first();
        velocity = new Vector2();
        position = new Vector2(
                StarGame.WORLD_WIDTH / 2 - WIDTH / 2,
                HEIGHT / 2 + 100f
        );
        respawnPosition = new Vector2(StarGame.WORLD_WIDTH / 2 - WIDTH / 2,
                HEIGHT / 2 + 100f);
        targetPosition = new Vector2();
        hitBox = new Rectangle(
                position.x - WIDTH / 2,
                position.y - HEIGHT / 2,
                WIDTH,
                HEIGHT
        );
        hitCircle = new Circle(position, WIDTH / 2);
    }

    private void update() {
        hitBox.set(
                position.x - WIDTH / 2,
                position.y - HEIGHT / 2,
                WIDTH,
                HEIGHT
        );
        hitCircle.setPosition(position);
    }

    // Метод, который занимается выстреливанием пули
    public void fire() {
        Bullet[] bl = BulletEmitter.getInstance().bullets;
        for (Bullet b : bl) {
            if (!b.active) {
                b.fireBullet(
                        position.x,
                        position.y,
                        400 * MathUtils.cosDeg(90),
                        400 * MathUtils.sinDeg(90)
                );
                break;
            }
        }
    }

    public void render(SpriteBatch batch, float delta) {
        //updateMovement(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            fire();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            flyLeft(delta);

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            flyRight(delta);
//            angle += rotationSpeed * delta;
//            if (angle >= 360){
//                angle = 0;
//            }
//            System.out.println("angle: " + angle);
//            System.out.println("direction: " + position.cpy().nor());

        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            flyUp(delta);

        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            flyDown(delta);

        } else if (isTargetSet) {
            goToTheTarget(delta);

        } else {
            region = animation.getIdle().first();
        }
        update();


        batch.draw(
                region,
                position.x - region.getRegionWidth() / 2,
                position.y - region.getRegionHeight() / 2,
                region.getRegionWidth() / 2, region.getRegionHeight() / 2,
                region.getRegionWidth(), region.getRegionHeight(),
                1f, 1f, angle
        );
    }

//    // Если угол до точки отличается от текущего угла корабля, стараемся развернуться в нужную сторону
//                if (angle > ang) {
//        if (angle - ang < PI) {
//            angle -= rotationSpeed * dt;
//        } else {
//            angle += rotationSpeed * dt;
//        }
//    }
//                if (angle < ang) {
//        if (ang - angle < PI) {
//            angle += rotationSpeed * dt;
//        } else {
//            angle -= rotationSpeed * dt;
//        }
//    }
//// Увеличиваем мощность двигателя
//    currentEnginePower += 100 * dt;
//                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
//                velocity.add((float) (currentEnginePower * cos(angle) * dt), (float) (currentEnginePower * sin(angle) * dt));
//}
//        }
//// Если игра запущена на десктопе,
//                if (!StarGame.isAndroid) {
//// управление реализуется на клавиатуре
//                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//                currentEnginePower = lowEnginePower;
//                }
//                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//                currentEnginePower += 100 * dt;
//                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
//                velocity.add((float) (currentEnginePower * cos(angle) * dt), (float) (currentEnginePower * sin(angle) * dt));
//                }
//                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//                angle += rotationSpeed * dt;
//                }
//                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                angle -= rotationSpeed * dt;
//                }
//                if (Gdx.input.isKeyPressed(Input.Keys.L)) {
//                fireCounter += dt;
//                if (fireCounter > fireRate) {
//                fireCounter = 0;
//                fire();
//                }
//                }
//                }
//// Угол корабля держим в пределах от -PI до PI
//                if (angle < -PI) angle += 2 * PI;
//        if (angle > PI) angle -= 2 * PI;
//// Если корабль улетел за экран, перебрасываем его на другую сторону
//        if (position.y > 752) position.y = -32;
//        if (position.y < -32) position.y = 752;
//        if (position.x > 1312) position.x = -32;
//        if (position.x < -32) position.x = 1312;
//// Перемещаем хитбокс за кораблем
//        hitArea.x = position.x;
//        hitArea.y = position.y;
//        }


    private void goToTheTarget(float delta) {

        float distance = targetPosition.dst(position);

        Vector2 d = targetPosition.cpy().sub(position.cpy()).nor();
        //angle = d.angle();

        position.add(
                targetPosition
                        .cpy()
                        .sub(position.cpy())
                        .nor()
                        .scl(delta * enginePower)
        );
        System.out.println(targetPosition.cpy().nor());

        if (distance < 1) {
            isTargetSet = false;
        }
    }

    private void updateMovement(float delta) {
        //Если игра запущена на десктопе,
        if (app.getType() != Application.ApplicationType.Android) {
            // управление реализуется на клавиатуре
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                enginePower = lowEnginePower;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                enginePower += maxEnginePower * delta;
                if (enginePower > maxEnginePower) enginePower = maxEnginePower;
                position.add(
                        (enginePower * cos(angle) * delta),
                        (enginePower * sin(angle) * delta)
                );
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                angle += rotationSpeed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                angle -= rotationSpeed * delta;
            }
        }

        // Угол корабля держим в пределах от -PI до PI
//        if (angle < -PI) angle += 2 * PI;
//        if (angle > PI) angle -= 2 * PI;
    }

    private void flyLeft(float delta) {
        region = animation.getAnimationLeft().getKeyFrame(delta);
        position.x -= delta * enginePower;
    }

    private void flyRight(float delta) {
        region = animation.getAnimationRight().getKeyFrame(delta);
        position.x += delta * enginePower;
    }

    private void flyUp(float delta) {
        region = animation.getIdle().first();
        position.y += delta * enginePower;
    }

    private void flyDown(float delta) {
        region = animation.getIdle().first();
        position.y -= delta * enginePower;
    }

    public void respawn() {
        position = respawnPosition.cpy();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitCircle() {
        return hitCircle;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    public boolean isTargetSet() {
        return isTargetSet;
    }

    public void setTargetSet(boolean targetSet) {
        isTargetSet = targetSet;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives += lives;
    }

}
