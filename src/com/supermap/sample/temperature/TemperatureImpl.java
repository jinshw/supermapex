package com.supermap.sample.temperature;

import java.util.HashMap;
import java.util.List;

import com.supermap.services.components.Component;
import com.supermap.services.components.ComponentContext;
import com.supermap.services.components.ComponentContextAware;
import com.supermap.services.components.commontypes.*;
import com.supermap.services.components.spi.MapProvider;

@Component(providerTypes = { MapProvider.class, TemperatureProvider.class }, optional = false, type = "")
public class TemperatureImpl implements Temperature, ComponentContextAware {

    private MapProvider mapProvider = null;
    private TemperatureProvider temperatureProvider = null;
    private MapParameter defaultMapParam = null;

    public TemperatureImpl() {
    }

    @Override
    public String getTemperature(String cityName) {
        String temp = null;
        try {
            temp = temperatureProvider.GetTemperature(cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    @Override
    public String getMapImage(String cityName) {
        String imageUrl = null;

        if (defaultMapParam != null) {
            ImageOutputOption imageOutputOption = new ImageOutputOption();
            imageOutputOption.format = OutputFormat.JPG;
            imageOutputOption.transparent = false;

            ThemeLabel themeLabel = new ThemeLabel();
            themeLabel.memoryData = new HashMap<String, String>();

            String temp = cityName + "，" + getTemperature(cityName);
            themeLabel.memoryData.put(cityName, temp);
            themeLabel.labelExpression = "AdminName";
            themeLabel.labelBackShape = LabelBackShape.ROUNDRECT;
            Style style = new Style();
            style.fillBackColor = new Color(java.awt.Color.MAGENTA.getRed(), java.awt.Color.MAGENTA.getGreen(), java.awt.Color.MAGENTA.getBlue());
            style.fillBackOpaque = true;
            style.fillForeColor = new Color(java.awt.Color.YELLOW.getRed(), java.awt.Color.YELLOW.getGreen(), java.awt.Color.YELLOW.getBlue());
            style.fillGradientMode = FillGradientMode.RADIAL;
            themeLabel.backStyle = style;
            TextStyle textStyle = new TextStyle();
            textStyle.backColor = new Color(java.awt.Color.BLUE.getRed(), java.awt.Color.BLUE.getGreen(), java.awt.Color.BLUE.getBlue());
            textStyle.fontWidth = 100000;
            textStyle.fontHeight = 100000;
            textStyle.align = TextAlignment.MIDDLECENTER;
            themeLabel.uniformStyle = textStyle;
            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
            datasetVectorInfo.name = "China_Capital_pt";
//            datasetVectorInfo.name = "China_Capital_P";
            datasetVectorInfo.type = DatasetType.POINT;
            datasetVectorInfo.dataSourceName = "China";
//            datasetVectorInfo.dataSourceName = "China400";
            UGCThemeLayer themelayer = new UGCThemeLayer();
            themelayer.theme = themeLabel;
            themelayer.datasetInfo = datasetVectorInfo.copy();
            themelayer.visible = true;
            themelayer.displayFilter = "AdminName = '" + cityName + "'";

            this.defaultMapParam.layers.get(0).subLayers.add(true, themelayer);
            this.defaultMapParam.scale = 0.00000003;
            Point2D center = this.getPosition(cityName);
            if (center != null) {
                this.defaultMapParam.center = new Point2D(center);
                this.defaultMapParam.rectifyType = RectifyType.BYCENTERANDMAPSCALE;
            }
            this.defaultMapParam.cacheEnabled = false;
            MapImage mapImage = mapProvider.getMapImage(this.defaultMapParam, imageOutputOption);
            imageUrl = mapImage.imageUrl;
            this.defaultMapParam.layers.get(0).subLayers.remove(themelayer.name);
        }
        return imageUrl;
    }

    private Point2D getPosition(String cityName) {
        Point2D point2D = null;

        QueryParameter queryParam = new QueryParameter();
//        queryParam.name = "China_PreCenCity_pt@China";
        queryParam.name = "China_Capital_pt@China";
        QueryParameterSet queryParamSet = new QueryParameterSet();
        queryParamSet.queryParams = new QueryParameter[1];
        queryParamSet.queryParams[0] = queryParam;
        queryParamSet.expectCount = 1;
        queryParamSet.queryOption = QueryOption.ATTRIBUTEANDGEOMETRY;
        queryParam.attributeFilter = "AdminName like '%" + cityName + "%'";
        QueryResult queryResult = this.mapProvider.queryBySQL(this.defaultMapParam.name, queryParamSet);
        if (queryResult != null) {
            if (queryResult.recordsets != null && queryResult.recordsets.length == 1) {
                Recordset recordset = queryResult.recordsets[0];
                // 首都为点图层，只有一个点位置信息
                if (recordset.features != null && recordset.features.length > 0) {
                    Geometry geometry = recordset.features[0].geometry;
                    point2D = geometry.points[0];
                }
            }
        }

        return point2D;
    }

    @Override
    public void setComponentContext(ComponentContext context) {
        TemperatureParam param = context.getConfig(TemperatureParam.class);
        if (param == null) {
            throw new IllegalArgumentException("参数 TemperatureParam 不能为空");
        }
        List<Object> providers = context.getProviders(Object.class);
        if (providers != null) {
            for (Object provider : providers) {
                if (provider instanceof TemperatureProvider) {
                    this.temperatureProvider = (TemperatureProvider) provider;
                    break;
                }
            }
            for (Object provider : providers) {
                if (provider instanceof MapProvider) {
                    this.mapProvider = (MapProvider) provider;
                    this.defaultMapParam = this.mapProvider.getMapParameter(param.getMapName());
                    this.defaultMapParam.viewer = new Rectangle(new Point(0, 0), new Point(800, 600));
                }
            }
        }
    }
}
