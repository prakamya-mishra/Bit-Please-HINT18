package hint.bitplease;

/**
 * Created by shashvatkedia on 17/03/18.
 */

public class SensorData {
        public float xCoordinate;
        public float yCoordinate;
        public float distance;

        public SensorData(float x,float y,float measuredDistance){
            xCoordinate = x;
            yCoordinate = y;
            distance = measuredDistance;
        }
}
