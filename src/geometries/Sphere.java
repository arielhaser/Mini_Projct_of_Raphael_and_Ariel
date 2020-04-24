package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * define Sphere as a shape of RadialGeometry
 */
public class Sphere extends RadialGeometry implements Geometry {
    protected Point3D _center;

    /**
     * constructor of Sphere
     * @param _radius = radius of Sphere
     * @param _center = the point of the center of the Sphere
     */
    public Sphere(double _radius, Point3D _center) {
        super(_radius);
        this._center = _center;
    }

    public Point3D get_center() {
        return _center;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "_center=" + _center +
                ", _radius=" + _radius +
                '}';
    }

    /**
     * the function allows to get the normal of the sphere
     * @param p: the point
     * @return the normal vector of the sphere
     */
    @Override
    public Vector getNormal(Point3D p) {
        Vector temp_vector = p.subtract(_center);
        return temp_vector.normalized();
    }

    /**
     *  this function allows to find the intersections between the ray and the sphere
     * @param ray: the ray
     * @return a list : result => if null: there isn't intersection and if not null,
     *         it includes all intersections
     */
    @Override
    public List<Point3D> findIntersections(Ray ray) {
        Point3D p0 = ray.get_p00();
        Vector v = ray.get_direction();

        if(_center.equals(p0)){
            List<Point3D> results = new ArrayList<>();
            results.add(new Point3D(ray.getPoint(_radius)));
            return results;
        }
        Vector u = _center.subtract(p0);
        double t1, t2;
        if(u.isSameVector(v) && !u.isOppositeDirection(v)){ // it's the continuation of the radius
            t1 = alignZero(u.length()-_radius);
            t2 = alignZero(u.length()+_radius);
        }
        else{
            double tm = v.dotProduct(u);
            double d = Math.sqrt(u.lengthSquared() - tm * tm);
            if (d > _radius) // there isn't intersection
                return null;

            double th = Math.sqrt(_radius * _radius - d * d);
            if (th == 0 ) return null;
            t1 = alignZero(tm - th);
            t2 = alignZero(tm + th);
        }
        if((t1<=0 && t2<=0)||(t1==t2))// there isn't intersection
            return null;

        List<Point3D> results = new ArrayList<>();
        if(t1>0){
            results.add(new Point3D(ray.getPoint(t1)));
        }
        if(t2>0){
            results.add(new Point3D(ray.getPoint(t2)));
        }
        return results;
    }
}
