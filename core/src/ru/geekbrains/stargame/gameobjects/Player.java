package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.StarGame;
import ru.geekbrains.stargame.animations.PlayerAnimation;

import static com.badlogic.gdx.Gdx.app;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;


public class Player {
    private PlayerAnimation animation;
    private TextureRegion region;
    private Vector2 position;
    private float lowEnginePower = 50f;
    private float enginePower = 200f;
    private float maxEnginePower = 300f;
    private float rotationSpeed = 200f;
    private Vector2 targetPosition;
    private boolean isTargetSet = false;
    private float angle = 0f;

    private Rectangle hitBox;

    public Player(TextureAtlas atlas) {
        animation = new PlayerAnimation(atlas);
        region = animation.getIdle().first();
        position = new Vector2(
                StarGame.WORLD_WIDTH / 2 - region.getRegionWidth() / 2,
                region.getRegionHeight() / 2 + 100f
        );
        targetPosition = new Vector2();
        hitBox = new Rectangle(
                position.x,
                position.y,
                80f,
                80f
        );
        System.out.println(position);
        System.out.println(hitBox);
    }

    private void update() {
        hitBox.set(
                position.x,
                position.y,
                region.getRegionWidth(),
                region.getRegionHeight()
        );
    }


    public void render(SpriteBatch batch, float delta) {
        //updateMovement(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            flyLeft(delta);

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            flyRight(delta);

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
        Vector2 d = position.cpy().nor();
        //System.out.println(d);
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
        angle = d.angle() - 90;

        position.add(targetPosition.cpy().sub(position.cpy()).nor().scl(delta * enginePower));

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


    public Vector2 getPosition() {
        return position;
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


}
