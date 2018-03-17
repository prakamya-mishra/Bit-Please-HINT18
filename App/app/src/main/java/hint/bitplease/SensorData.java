package hint.bitplease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shashvatkedia on 17/03/18.
 */

public class SensorData {
        public float gyroX;
        public float gyroY;
        public float gyroZ;
        public float distance;
        private static float[] distanceRightArray;
        private static float[] distanceLeftArray;
        public List distanceLeft;
        public List distanceRight;

        public SensorData(float x,float y,float z,float measuredDistance,float left,float right){
            gyroX = x;
            gyroY = y;
            gyroZ = z;
            distance = measuredDistance;
            if(SensorData.distanceLeftArray.length == 10){
                updateArray(distanceLeftArray,left);
            }
            if(SensorData.distanceRightArray.length == 10){
                updateArray(distanceRightArray,right);
            }
            distanceLeft = new ArrayList<String>();
            distanceRight = new ArrayList<String>();
        }

        public static void updateArray(float[] array,float value){
            for(int i = 0;i <= array.length - 1;i++){

            }
        }
}
