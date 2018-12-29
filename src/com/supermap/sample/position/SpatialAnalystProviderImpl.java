package com.supermap.sample.position;

import com.supermap.services.components.commontypes.*;
import com.supermap.services.components.spi.SpatialAnalystProvider;

import java.util.List;

public class SpatialAnalystProviderImpl implements SpatialAnalystProvider {
    @Override
    public List<String> getDatasourceNames() {
        return null;
    }

    @Override
    public List<String> getDatasetNames(String s) {
        return null;
    }

    @Override
    public DatasetInfo getDatasetInfo(String s, String s1) {
        return null;
    }

    @Override
    public boolean deleteDataset(String s, String s1) {
        return false;
    }

    @Override
    public GeometrySpatialAnalystResult buffer(Geometry geometry, BufferAnalystParameter bufferAnalystParameter, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult buffer(GeometryWithPrjCoordSys geometryWithPrjCoordSys, BufferAnalystParameter bufferAnalystParameter, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult buffer(String s, QueryParameter queryParameter, BufferAnalystParameter bufferAnalystParameter, BufferResultSetting bufferResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult erase(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult erase(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult erase(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult identity(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult identity(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult identity(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult intersect(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult intersect(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult intersect(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult union(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult union(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult union(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult update(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult update(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult update(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult clip(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult clip(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult clip(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult xor(String s, QueryParameter queryParameter, String s1, QueryParameter queryParameter1, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult xor(String s, QueryParameter queryParameter, Geometry[] geometries, DatasetOverlayResultSetting datasetOverlayResultSetting) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult xor(Geometry geometry, Geometry geometry1, GeometrySpatialAnalystResultSetting geometrySpatialAnalystResultSetting) {
        return null;
    }

    @Override
    public ProximityAnalystResult createThiessenPolygon(ProximityAnalystParameterForDatasetInput proximityAnalystParameterForDatasetInput) {
        return null;
    }

    @Override
    public ProximityAnalystResult createThiessenPolygon(ProximityAnalystParameterForPointsInput proximityAnalystParameterForPointsInput) {
        return null;
    }

    @Override
    public ComputeDistanceResult computeMinDistance(ComputeMinDistanceParameterForDatasetInput computeMinDistanceParameterForDatasetInput) {
        return null;
    }

    @Override
    public ComputeDistanceResult computeMinDistance(ComputeMinDistanceParameterForGeometriesInput computeMinDistanceParameterForGeometriesInput) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoline(String s, QueryParameter queryParameter, String s1, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoline(String s, QueryParameter queryParameter, String s1, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption, InterpolateType interpolateType) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoline(String s, ExtractParameter extractParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoline(Point2D[] point2DS, double[] doubles, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoline(Point2D[] point2DS, double[] doubles, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption, InterpolateType interpolateType) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoregion(String s, QueryParameter queryParameter, String s1, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption, InterpolateType interpolateType) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoregion(String s, QueryParameter queryParameter, String s1, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoregion(String s, ExtractParameter extractParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoregion(Point2D[] point2DS, double[] doubles, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractIsoregion(Point2D[] point2DS, double[] doubles, double v, ExtractParameter extractParameter, DataReturnOption dataReturnOption, InterpolateType interpolateType) {
        return null;
    }

    @Override
    public InterpolationResult interpolate(InterpolationParameter interpolationParameter) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult generateSpatialData(GenerateSpatialDataParameter generateSpatialDataParameter, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public GeoRelationResult<?>[] geoRelation(GeoRelationParameter geoRelationParameter) {
        return new GeoRelationResult[0];
    }

    @Override
    public DatasetSpatialAnalystResult calculateAspect(String s, TerrainAnalystSetting terrainAnalystSetting, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public List<DatasetSpatialAnalystResult> calculateCurvature(String s, double v, String s1, String s2, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult calculateSlope(String s, TerrainAnalystSetting terrainAnalystSetting, SlopeType slopeType, double v, DataReturnOption dataReturnOption) {
        return null;
    }

    @Override
    public double computeSurfaceArea(String s, Geometry geometry) {
        return 0;
    }

    @Override
    public double computeSurfaceDistance(String s, Geometry geometry) {
        return 0;
    }

    @Override
    public ProfileResult calculateProfile(String s, Geometry geometry, double v) {
        return null;
    }

    @Override
    public ProfileResult calculateProfile(String s, String s1, double v) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult calculatePlumbProfile(Point2D point2D, Point2D point2D1, String[] strings, String s, boolean b) {
        return null;
    }

    @Override
    public CostPathLineResult costPathLine(String s, Point2D point2D, Point2D point2D1, SmoothMethod smoothMethod, int i, DataReturnOption dataReturnOption, double v, double v1) {
        return null;
    }

    @Override
    public CutFillResult cutFill(String s, String s1, String s2, boolean b, boolean b1) {
        return null;
    }

    @Override
    public CutFillResult cutFill(String s, Geometry geometry, double v, String s1, boolean b, boolean b1) {
        return null;
    }

    @Override
    public CutFillResult cutFill(String s, Geometry geometry, double v, boolean b, String s1, boolean b1, boolean b2) {
        return null;
    }

    @Override
    public CutFillResult cutFill(String s, Geometry3D geometry3D, String s1, boolean b, boolean b1) {
        return null;
    }

    @Override
    public double cutFill(String s, double v, boolean b, Geometry geometry) {
        return 0;
    }

    @Override
    public GeometrySpatialAnalystResult flood(String s, double v, Geometry geometry) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractValleyLine(String s, Geometry geometry, String s1, String s2, boolean b) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult extractRidgeLine(String s, Geometry geometry, String s1, String s2, boolean b) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult executeMathAnalystExpression(String s, Geometry geometry, boolean b, boolean b1, String s1, String s2, boolean b2) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult locatePoint(Route route, double v, double v1, boolean b) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult locatePoint(String s, String s1, String s2, double v, double v1, boolean b) {
        return null;
    }

    @Override
    public GeometrySpatialAnalystResult locateLine(Route route, double v, double v1) {


        return null;
    }

    @Override
    public GeometrySpatialAnalystResult locateLine(String s, String s1, String s2, double v, double v1) {
        return null;
    }

    @Override
    public RouteCalculateMeasureResult calculateMeasureAtPoint(Route route, Point2D point2D, double v, boolean b) {
        return null;
    }

    @Override
    public DatasetSpatialAnalystResult kernelDensity(DensityAnalystParameterInput densityAnalystParameterInput, String s, String s1, String s2, String s3, boolean b) {
        return null;
    }
}
