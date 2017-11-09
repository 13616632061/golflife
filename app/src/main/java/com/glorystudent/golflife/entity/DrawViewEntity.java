package com.glorystudent.golflife.entity;

import java.util.List;

/**
 * Created by Gavin.J on 2017/11/9.
 */

public class DrawViewEntity {
    private boolean isTemp;
    private float currentDrawViewHeight;
    private float currentDrawViewWidth;
    private String drawDataName;
    private String voiceName;
    private String zipName;
    private String zipFileMD5;
    private float transformToPTRate;
    private List<RectArrBean> rectArr;
    private List<Integer> viewScaleVoiceBeginTimeArr;
    private List<Integer> viewScaleArr;
    private List<CircleArrBean> circleArr;
    private List<Integer> lineScaleArr;
    private List<ArrowArrBean> arrowArr;
    private List<Float> viewScaleVoiceEndTimeArr;
    private List<CurveArrBean> curveArr;
    private List<AngelArrBean> angelArr;
    private List<String> drawColorArr;
    private List<LineArrBean> lineArr;
    private List<VoiceTimeBean> voiceTimeArr;
    private List<Integer> drawTypeArr;
    private List<VoiceArrBean> voiceArr;

    public float getCurrentDrawViewHeight() {
        return currentDrawViewHeight;
    }

    public void setCurrentDrawViewHeight(float currentDrawViewHeight) {
        this.currentDrawViewHeight = currentDrawViewHeight;
    }

    public float getCurrentDrawViewWidth() {
        return currentDrawViewWidth;
    }

    public void setCurrentDrawViewWidth(float currentDrawViewWidth) {
        this.currentDrawViewWidth = currentDrawViewWidth;
    }

    public float getTransformToPTRate() {
        return transformToPTRate;
    }

    public void setTransformToPTRate(float transformToPTRate) {
        this.transformToPTRate = transformToPTRate;
    }

    public boolean isIsTemp() {
        return isTemp;
    }

    public void setIsTemp(boolean isTemp) {
        this.isTemp = isTemp;
    }

    public String getDrawDataName() {
        return drawDataName;
    }

    public void setDrawDataName(String drawDataName) {
        this.drawDataName = drawDataName;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public String getZipFileMD5() {
        return zipFileMD5;
    }

    public void setZipFileMD5(String zipFileMD5) {
        this.zipFileMD5 = zipFileMD5;
    }

    public List<RectArrBean> getRectArr() {
        return rectArr;
    }

    public void setRectArr(List<RectArrBean> rectArr) {
        this.rectArr = rectArr;
    }

    public List<Integer> getViewScaleVoiceBeginTimeArr() {
        return viewScaleVoiceBeginTimeArr;
    }

    public void setViewScaleVoiceBeginTimeArr(List<Integer> viewScaleVoiceBeginTimeArr) {
        this.viewScaleVoiceBeginTimeArr = viewScaleVoiceBeginTimeArr;
    }

    public List<Integer> getViewScaleArr() {
        return viewScaleArr;
    }

    public void setViewScaleArr(List<Integer> viewScaleArr) {
        this.viewScaleArr = viewScaleArr;
    }

    public List<CircleArrBean> getCircleArr() {
        return circleArr;
    }

    public void setCircleArr(List<CircleArrBean> circleArr) {
        this.circleArr = circleArr;
    }

    public List<Integer> getLineScaleArr() {
        return lineScaleArr;
    }

    public void setLineScaleArr(List<Integer> lineScaleArr) {
        this.lineScaleArr = lineScaleArr;
    }

    public List<ArrowArrBean> getArrowArr() {
        return arrowArr;
    }

    public void setArrowArr(List<ArrowArrBean> arrowArr) {
        this.arrowArr = arrowArr;
    }

    public List<Float> getViewScaleVoiceEndTimeArr() {
        return viewScaleVoiceEndTimeArr;
    }

    public void setViewScaleVoiceEndTimeArr(List<Float> viewScaleVoiceEndTimeArr) {
        this.viewScaleVoiceEndTimeArr = viewScaleVoiceEndTimeArr;
    }

    public List<CurveArrBean> getCurveArr() {
        return curveArr;
    }

    public void setCurveArr(List<CurveArrBean> curveArr) {
        this.curveArr = curveArr;
    }

    public List<AngelArrBean> getAngelArr() {
        return angelArr;
    }

    public void setAngelArr(List<AngelArrBean> angelArr) {
        this.angelArr = angelArr;
    }

    public List<String> getDrawColorArr() {
        return drawColorArr;
    }

    public void setDrawColorArr(List<String> drawColorArr) {
        this.drawColorArr = drawColorArr;
    }

    public List<LineArrBean> getLineArr() {
        return lineArr;
    }

    public void setLineArr(List<LineArrBean> lineArr) {
        this.lineArr = lineArr;
    }

    public List<VoiceTimeBean> getVoiceTimeArr() {
        return voiceTimeArr;
    }

    public void setVoiceTimeArr(List<VoiceTimeBean> voiceTimeArr) {
        this.voiceTimeArr = voiceTimeArr;
    }

    public List<Integer> getDrawTypeArr() {
        return drawTypeArr;
    }

    public void setDrawTypeArr(List<Integer> drawTypeArr) {
        this.drawTypeArr = drawTypeArr;
    }

    public List<VoiceArrBean> getVoiceArr() {
        return voiceArr;
    }

    public void setVoiceArr(List<VoiceArrBean> voiceArr) {
        this.voiceArr = voiceArr;
    }

    @Override
    public String toString() {
        return "DrawViewEntity{" +
                "isTemp=" + isTemp +
                ", drawDataName='" + drawDataName + '\'' +
                ", voiceName='" + voiceName + '\'' +
                ", zipName='" + zipName + '\'' +
                ", zipFileMD5='" + zipFileMD5 + '\'' +
                ", rectArr=" + rectArr +
                ", viewScaleVoiceBeginTimeArr=" + viewScaleVoiceBeginTimeArr +
                ", viewScaleArr=" + viewScaleArr +
                ", circleArr=" + circleArr +
                ", lineScaleArr=" + lineScaleArr +
                ", arrowArr=" + arrowArr +
                ", viewScaleVoiceEndTimeArr=" + viewScaleVoiceEndTimeArr +
                ", curveArr=" + curveArr +
                ", angelArr=" + angelArr +
                ", drawColorArr=" + drawColorArr +
                ", lineArr=" + lineArr +
                ", voiceTimeArr=" + voiceTimeArr +
                ", drawTypeArr=" + drawTypeArr +
                ", voiceArr=" + voiceArr +
                '}';
    }

    public static class VoiceTimeBean{
        Float beginTime;
        Float endTime;
        List<Float> changeTimeArr;

        public Float getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Float beginTime) {
            this.beginTime = beginTime;
        }

        public Float getEndTime() {
            return endTime;
        }

        public void setEndTime(Float endTime) {
            this.endTime = endTime;
        }

        public List<Float> getChangeTimeArr() {
            return changeTimeArr;
        }

        public void setChangeTimeArr(List<Float> changeTimeArr) {
            this.changeTimeArr = changeTimeArr;
        }
    }

    public static class RectArrBean {
        /**
         * endPoint : {"x":285.3333282470703,"y":291.3333282470703}
         * firstPoint : {"x":222,"y":202.3333282470703}
         * changePointArr : []
         */

        private EndPointBean endPoint;
        private FirstPointBean firstPoint;
        private List<CurveArrBean.ChangePointArrBean> changePointArr;

        public EndPointBean getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(EndPointBean endPoint) {
            this.endPoint = endPoint;
        }

        public FirstPointBean getFirstPoint() {
            return firstPoint;
        }

        public void setFirstPoint(FirstPointBean firstPoint) {
            this.firstPoint = firstPoint;
        }

        public List<CurveArrBean.ChangePointArrBean> getChangePointArr() {
            return changePointArr;
        }

        public void setChangePointArr(List<CurveArrBean.ChangePointArrBean> changePointArr) {
            this.changePointArr = changePointArr;
        }

        public static class EndPointBean {
            /**
             * x : 285.3333282470703
             * y : 291.3333282470703
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }

        public static class FirstPointBean {
            /**
             * x : 222
             * y : 202.3333282470703
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }
    }

    public static class CircleArrBean {
        /**
         * endPoint : {"x":286,"y":214}
         * firstPoint : {"x":238.3333282470703,"y":153.6666564941406}
         * changePointArr : []
         */

        private EndPointBeanX endPoint;
        private FirstPointBeanX firstPoint;
        private CenterPointBeanX centerPoint;
        private List<CurveArrBean.ChangePointArrBean> changePointArr;

        public CenterPointBeanX getCenterPoint() {
            return centerPoint;
        }

        public void setCenterPoint(CenterPointBeanX centerPoint) {
            this.centerPoint = centerPoint;
        }

        public EndPointBeanX getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(EndPointBeanX endPoint) {
            this.endPoint = endPoint;
        }

        public FirstPointBeanX getFirstPoint() {
            return firstPoint;
        }

        public void setFirstPoint(FirstPointBeanX firstPoint) {
            this.firstPoint = firstPoint;
        }

        public List<CurveArrBean.ChangePointArrBean> getChangePointArr() {
            return changePointArr;
        }

        public void setChangePointArr(List<CurveArrBean.ChangePointArrBean> changePointArr) {
            this.changePointArr = changePointArr;
        }
        public static class CenterPointBeanX{
            /**
             * x : 286
             * y : 214
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }


        public static class EndPointBeanX {
            /**
             * x : 286
             * y : 214
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }

        public static class FirstPointBeanX {
            /**
             * x : 238.3333282470703
             * y : 153.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }
    }

    public static class ArrowArrBean {
        /**
         * endPoint : {"x":199.6666564941406,"y":412.6666564941406}
         * firstPoint : {"x":301.6666564941406,"y":259.6666564941406}
         * changePointArr : []
         */

        private EndPointBeanXX endPoint;
        private FirstPointBeanXX firstPoint;
        private List<CurveArrBean.ChangePointArrBean> changePointArr;

        public EndPointBeanXX getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(EndPointBeanXX endPoint) {
            this.endPoint = endPoint;
        }

        public FirstPointBeanXX getFirstPoint() {
            return firstPoint;
        }

        public void setFirstPoint(FirstPointBeanXX firstPoint) {
            this.firstPoint = firstPoint;
        }

        public List<CurveArrBean.ChangePointArrBean> getChangePointArr() {
            return changePointArr;
        }

        public void setChangePointArr(List<CurveArrBean.ChangePointArrBean> changePointArr) {
            this.changePointArr = changePointArr;
        }

        public static class EndPointBeanXX {
            /**
             * x : 199.6666564941406
             * y : 412.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }

        public static class FirstPointBeanXX {
            /**
             * x : 301.6666564941406
             * y : 259.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }
    }

    public static class CurveArrBean {
        /**
         * firstPoint : {"x":227.3333282470703,"y":199.6666564941406}
         * changePointArr : [{"x":216.6666564941406,"y":208.6666564941406},{"x":216.6666564941406,"y":208.6666564941406},{"x":204.3333282470703,"y":222.3333282470703},{"x":185,"y":247.3333282470703},{"x":182,"y":257},{"x":182,"y":266.3333282470703},{"x":182.6666564941406,"y":273.6666564941406},{"x":188.3333282470703,"y":279},{"x":191.3333282470703,"y":282.3333282470703},{"x":193.3333282470703,"y":284.6666564941406},{"x":193.3333282470703,"y":289.3333282470703},{"x":178.3333282470703,"y":295.3333282470703},{"x":157,"y":303.3333282470703},{"x":134.6666564941406,"y":310.6666564941406},{"x":112.3333282470703,"y":316},{"x":90.33332824707031,"y":319.6666564941406},{"x":73,"y":322},{"x":59,"y":323.3333282470703},{"x":46.66665649414062,"y":324.6666564941406},{"x":39.33332824707031,"y":326},{"x":35.66665649414062,"y":326.6666564941406},{"x":34.33332824707031,"y":327.3333282470703},{"x":34.33332824707031,"y":328.6666564941406},{"x":38,"y":329},{"x":46,"y":330},{"x":53.33332824707031,"y":331.3333282470703},{"x":58.66665649414062,"y":333},{"x":63.66665649414062,"y":335.3333282470703},{"x":67.33332824707031,"y":337},{"x":70,"y":338.6666564941406},{"x":72,"y":340},{"x":73.33332824707031,"y":340.6666564941406},{"x":74.33332824707031,"y":341.3333282470703},{"x":75.66665649414062,"y":341.6666564941406},{"x":77,"y":342},{"x":78,"y":342.3333282470703},{"x":79,"y":342.6666564941406},{"x":79.33332824707031,"y":343},{"x":80,"y":343.3333282470703},{"x":80.33332824707031,"y":343.3333282470703},{"x":80.66665649414062,"y":343.3333282470703},{"x":81,"y":343.3333282470703},{"x":81,"y":343.6666564941406},{"x":81.33332824707031,"y":343.6666564941406},{"x":81.66665649414062,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":343.6666564941406},{"x":82,"y":344},{"x":82.33332824707031,"y":344},{"x":82.66665649414062,"y":344},{"x":83.33332824707031,"y":344.3333282470703},{"x":84.33332824707031,"y":344.6666564941406},{"x":85.33332824707031,"y":345.3333282470703},{"x":86.33332824707031,"y":345.6666564941406},{"x":87.66665649414062,"y":346.6666564941406},{"x":89.33332824707031,"y":347.6666564941406},{"x":91,"y":348.6666564941406},{"x":92.33332824707031,"y":349.6666564941406},{"x":94.66665649414062,"y":351},{"x":97,"y":352.6666564941406},{"x":99.66665649414062,"y":354.6666564941406},{"x":101.6666564941406,"y":357.3333282470703},{"x":104.6666564941406,"y":360},{"x":107.6666564941406,"y":363.3333282470703},{"x":110.6666564941406,"y":367.3333282470703},{"x":115.3333282470703,"y":373},{"x":120.3333282470703,"y":379},{"x":126.6666564941406,"y":386},{"x":135,"y":395.3333282470703}]
         */

        private FirstPointBeanXXX firstPoint;
        private List<ChangePointArrBean> changePointArr;

        public FirstPointBeanXXX getFirstPoint() {
            return firstPoint;
        }

        public void setFirstPoint(FirstPointBeanXXX firstPoint) {
            this.firstPoint = firstPoint;
        }

        public List<ChangePointArrBean> getChangePointArr() {
            return changePointArr;
        }

        public void setChangePointArr(List<ChangePointArrBean> changePointArr) {
            this.changePointArr = changePointArr;
        }

        public static class FirstPointBeanXXX {
            /**
             * x : 227.3333282470703
             * y : 199.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }

        public static class ChangePointArrBean {
            /**
             * x : 216.6666564941406
             * y : 208.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }
    }

    public static class AngelArrBean {
        /**
         * changePointArr : []
         * endPoint : {"x":100.3333282470703,"y":368.6666564941406}
         * isShouldDrawCircle : false
         * firstPoint : {"x":202,"y":242}
         * otherFirstPoint : {"x":262,"y":302}
         */

        private EndPointBeanXXX endPoint;
        private boolean isShouldDrawCircle;
        private FirstPointBeanXXXX firstPoint;
        private OtherFirstPointBean otherFirstPoint;
        private List<CurveArrBean.ChangePointArrBean> changePointArr;

        public EndPointBeanXXX getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(EndPointBeanXXX endPoint) {
            this.endPoint = endPoint;
        }

        public boolean isIsShouldDrawCircle() {
            return isShouldDrawCircle;
        }

        public void setIsShouldDrawCircle(boolean isShouldDrawCircle) {
            this.isShouldDrawCircle = isShouldDrawCircle;
        }

        public FirstPointBeanXXXX getFirstPoint() {
            return firstPoint;
        }

        public void setFirstPoint(FirstPointBeanXXXX firstPoint) {
            this.firstPoint = firstPoint;
        }

        public OtherFirstPointBean getOtherFirstPoint() {
            return otherFirstPoint;
        }

        public void setOtherFirstPoint(OtherFirstPointBean otherFirstPoint) {
            this.otherFirstPoint = otherFirstPoint;
        }

        public boolean isShouldDrawCircle() {
            return isShouldDrawCircle;
        }

        public void setShouldDrawCircle(boolean shouldDrawCircle) {
            isShouldDrawCircle = shouldDrawCircle;
        }

        public List<CurveArrBean.ChangePointArrBean> getChangePointArr() {
            return changePointArr;
        }

        public void setChangePointArr(List<CurveArrBean.ChangePointArrBean> changePointArr) {
            this.changePointArr = changePointArr;
        }

        public static class EndPointBeanXXX {
            /**
             * x : 100.3333282470703
             * y : 368.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }

        public static class FirstPointBeanXXXX {
            /**
             * x : 202
             * y : 242
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }

        public static class OtherFirstPointBean {
            /**
             * x : 262
             * y : 302
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }
        }
    }

    public static class LineArrBean {
        /**
         * endPoint : {"x":97,"y":243}
         * firstPoint : {"x":188,"y":115.6666564941406}
         * changePointArr : []
         */

        private EndPointBeanXXXX endPoint;
        private FirstPointBeanXXXXX firstPoint;
        private List<CurveArrBean.ChangePointArrBean> changePointArr;

        public EndPointBeanXXXX getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(EndPointBeanXXXX endPoint) {
            this.endPoint = endPoint;
        }

        public FirstPointBeanXXXXX getFirstPoint() {
            return firstPoint;
        }

        public void setFirstPoint(FirstPointBeanXXXXX firstPoint) {
            this.firstPoint = firstPoint;
        }

        public List<CurveArrBean.ChangePointArrBean> getChangePointArr() {
            return changePointArr;
        }

        public void setChangePointArr(List<CurveArrBean.ChangePointArrBean> changePointArr) {
            this.changePointArr = changePointArr;
        }

        @Override
        public String toString() {
            return "LineArrBean{" +
                    "endPoint=" + endPoint +
                    ", firstPoint=" + firstPoint +
                    ", changePointArr=" + changePointArr +
                    '}';
        }

        public static class EndPointBeanXXXX {
            /**
             * x : 97
             * y : 243
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }

            @Override
            public String toString() {
                return "EndPointBeanXXXX{" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }
        }

        public static class FirstPointBeanXXXXX {
            /**
             * x : 188
             * y : 115.6666564941406
             */

            private float x;
            private float y;

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }

            @Override
            public String toString() {
                return "FirstPointBeanXXXXX{" +
                        "x=" + x +
                        ", y=" + y +
                        '}';
            }
        }
    }

    public static class VoiceArrBean {
        /**
         * voiceTime : 0.010557
         * videoProgress : 0
         */

        private float voiceTime;
        private float videoProgress;

        public float getVoiceTime() {
            return voiceTime;
        }

        public void setVoiceTime(float voiceTime) {
            this.voiceTime = voiceTime;
        }

        public float getVideoProgress() {
            return videoProgress;
        }

        public void setVideoProgress(float videoProgress) {
            this.videoProgress = videoProgress;
        }
    }
}
