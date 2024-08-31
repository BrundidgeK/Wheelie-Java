/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2024, Franklin Academy Robotics
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

public class PathFollower {
	public Path path;
	public Pose2D startPt;
	public double look;

	private int wayPoint = 0;
	private boolean shrinking_look;

	/** The constructor for the path follower, with the starting Pose2D, lookahead distance, and path */

	public PathFollower (Pose2D startPt, double look, Path path) {
		this.startPt = startPt;
		this.look = look;
		this.path = path;
	}

	public PathFollower (Pose2D startPt, double look) {
		this.startPt = startPt;
		this.look = look;
		path = new Path();
	}


	/**
	 * Returns the movement required the robot is from its next waypoint within its lookahead
	 * @param obj The location of the robot, AKA the center of the circle
	 *
	 * @author Kennedy Brundidge
	 */
	public Pose2D followPath(Pose2D obj){
		//Finds the distance between current position and the next waypoint
		double distance = Math.hypot(path.getPt(wayPoint).x - obj.x,
				path.getPt(wayPoint).y - obj.y);
		double lookAhead = look;

		//Ensures that the circle is still in contact with path
		if (distance < lookAhead){
			if (path.pathLength() != wayPoint+1)
				wayPoint++; //Increments to next waypoint if circle and path don't intersect
			else {
				lookAhead = distance; //Shrinks the circle as it approaches the last waypoint
				shrinking_look = true;
			}
		}

		//Finds the target in its lookahead distance
		Pose2D target = PursuitMath.waypointCalc
				(obj, lookAhead, path.getPt(wayPoint), path.getPt(wayPoint+1));

		Pose2D diff = new Pose2D(target.x - obj.x,
				target.y - obj.y,
				target.h - obj.h);

		//Finds the forward, strafe, and turn values
		double angle = Math.atan2(diff.y, diff.x);
		double forward = Math.cos(angle) * diff.x + Math.sin(angle) * diff.y,
				strafe = -Math.sin(angle) * diff.x + Math.cos(angle) * diff.y,
				turn = PursuitMath.Clamp(diff.h);

		return new Pose2D(forward, strafe, turn);
	}

	/** Returns the index of the current Pose2D in the Path */
	public int getWayPoint(){
		return wayPoint;
	}

	/** Returns if the lookahead distance is shrinking */
	public boolean isShrinkingLook(){
		return shrinking_look;
	}
}
