// Copyright 2000 softSurfer, 2012 Dan Sunday
// This code may be freely used and modified for any purpose
// providing that this copyright notice is included with it.
// SoftSurfer makes no warranty for this code, and cannot be held
// liable for any real or imagined damage resulting from its use.
// Users of this code must verify correctness for their application.

import scala.annotation.tailrec

class WindingNumber

object WindingNumber {


  // wn_PnPoly(): winding number test for a point in a polygon
  //      Input:   P = a point,
  //               V[] = vertex points of a polygon V[n+1] with V[n]=V[0]
  //      Return:  wn = the winding number (=0 only when P is outside)
  def WindingNumberPointPolygon(V: Polygon, P: Point): Int = {

    val n = V.points.length

    @tailrec
    def loop(V: Polygon, P: Point, wn: Int = 0, i: Int = 0): Int = {
      if (i >= n) wn
      else
        val nextIndex = (i + 1) % n
        val newWn =
          if (V.points(i).y <= P.y && V.points(nextIndex).y > P.y && IsLeft(V.points(i), V.points(nextIndex), P) > 0)
            wn + 1
          else if (V.points(i).y > P.y && V.points(nextIndex).y <= P.y && IsLeft(V.points(i), V.points(nextIndex), P) < 0)
            wn - 1
          else
            wn
        loop(V, P, newWn, i + 1)
    }

    loop(V, P)
  }

  // isLeft(): tests if a point is Left|On|Right of an infinite line.
  //    Input:  three points P0, P1, and P2
  //    Return: >0 for P2 left of the line through P0 and P1
  //            =0 for P2  on the line
  //            <0 for P2  right of the line
  //    See: Algorithm 1 "Area of Triangles and Polygons"
  private def IsLeft(P0: Point, P1: Point, P2: Point): Int = (P1.x - P0.x) * (P2.y - P0.y)
    - (P2.x - P0.x) * (P1.y - P0.y)
}
