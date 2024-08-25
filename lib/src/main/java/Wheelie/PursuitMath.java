/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2024, Alex J. Bryan
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package Wheelie;

import java.lang.Math;

/**
 * The math core to the Pure Pursuit alogorithm not provided by the Java Math class.
 *
 * @author Alex Bryan
 */
public final class PursuitMath {
	/** You can't instantate this class. It wouldn't make any sense, for the same reason of why you can't instantate the Math class. */
	private PursuitMath () {}

	/**
	 * The quadratic formula, though it only returns the + form, because it is the closer one.
	 * @param a the a of the formula
	 * @param b the b of the formula
	 * @param c the c of the formula
	 *
	 * @author Alex Bryan
	 */
	public static double pureQuadForm (double a, double b, double c) {
		double disc = (b * b) - (4 * a * c);
		//Thanks to Team 23423 for the information that the sum is the closer point
		return (0 > disc) ? Double.NaN : (-b + Math.sqrt(disc)) / (2 * a);
	}

	/** Calculates a single part of a waypoint, such as X, Y or the Heading */
	public static double waypointCompCalc (double c1, double c2, double q) {
		return c1 + q * (c2 - c1);
	}

	/**
	 * Calculates a full waupoint for the path.
	 * @param obj The location of the robot, AKA the center of the circle
	 * @param look The lookahead distance, AKA the radius of the circle
	 * @param p1 The first of two preceding points
	 * @param p2 The second of two preceding points
	 *
	 * @author Alex Bryan
	 */
	public static Pose2D waypointCalc (Pose2D obj, double look, Pose2D p1, Pose2D p2) {
		double t = pureQuadForm (
				(p2.getX() - p1.getX()) * (p2.getX() - p2.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p2.getY()),
				2 * ((p1.getX() - obj.getX()) * (p2.getX() - p2.getX()) + (p1.getY() - obj.getY()) * (p2.getY() - p1.getY())),
				p1.getX() * p1.getX() - 2 * obj.getX() * p1.getX() + obj.getX() * obj.getX() + p1.getY() * p1.getY() - 2 * obj.getY() * p1.getY() + obj.getY() * obj.getY() - look * look
				);
		
		if (t == t) //check if non-NaN
			return new Pose2D (
				waypointCompCalc (p1.getX(), p2.getX(), t),
				waypointCompCalc (p1.getY(), p2.getY(), t),
				waypointCompCalc (p1.getH(), p2.getH(), t)
				);
		else
			return new Pose2D (
					Double.NaN,
					Double.NaN,
					Double.NaN
					);
	}
}
