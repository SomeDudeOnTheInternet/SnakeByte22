

package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="TeleOp", group="4546")
public class Teleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor fL, bL, fR, bR; // front left, back left, front right, back right







    public double WeightAvg(double x, double y, double z) {
        double speed_D = 0;

        if ((Math.abs(x) + Math.abs(y) + Math.abs(z))  != 0.0) {
            speed_D = ((x * Math.abs(x)) + (y * Math.abs(y)) + (z * Math.abs(z)))
                    / (Math.abs(x) + Math.abs(y) + Math.abs(z));
        }
        return (speed_D);
    }






    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here as parameters
        fL  = hardwareMap.get(DcMotor.class, "fL");
        bL  = hardwareMap.get(DcMotor.class, "bL");
        fR  = hardwareMap.get(DcMotor.class, "fR");
        bR  = hardwareMap.get(DcMotor.class, "bR");
        fL.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.FORWARD);

        // Variables to store positions
        double sPos = .05;
        double hPos = .05;
        String liftState = "down";
        double wristDeposit = .6;
        double wristRest = .15;
        double wristTilt = .3;
        double wristGround = .8;
        double liftMax = 1250;
        double liftMin = 0;

        // Waits for the game to start (driver presses PLAY)

        waitForStart();
        runtime.reset();

        // starting program
        double power = 1.0;

        while (opModeIsActive()) {
            //Driver 1
            /*
            Driver 1 controls the drivetrain, carousel, intake, sorter, and the base level
            outtake. This ensures the maximum efficiency and speed possible as less coordination is
            needed between drivers for when it matters such as competing for tipping the shared
            shipping hub. Additionally, driver 1 should be able to spin the carousel wheel in either
            direction regardless of team color.
//////////////////////////////////////////////////////////////////////////////////////////////////
            POSSIBLE FUTURE CHANGES:
            Single button Macro
             */

            // Driving [arcade mode]
            if (Math.abs(gamepad1.left_stick_y) > .05 || Math.abs(gamepad1.left_stick_x) > .05 || Math.abs(gamepad1.right_stick_x) > .05) {
                driveTrainPower(-power*gamepad1.left_stick_y, -power*gamepad1.left_stick_x, -power*gamepad1.right_stick_x * .98);
            } else {
                driveTrainPower(0, 0, 0);
            }

            if (gamepad1.dpad_down) // Half-power mode
                power = 0.5;
            else if (gamepad1.dpad_up) // Full-power mode
                power = 1.0;


            // If the right trigger is pressed down past a threshhold, the intake will run at full
            // power. If left trigger, will run the opposite direction to clear the robot.
            // If neither condition is met, automatically set power to 0 and rest.



            // If the left bumper is pressed down past a threshhold, the carousel will run at half
            // power. If right bumper, will run the opposite direction to spin the carousel.
            // If neither condition is met, automatically set power to 0 and rest.
            if(gamepad1.left_bumper) {
                fL.setPower(.05);
                fR.setPower(.05);
                bL.setPower(.05);
                bR.setPower(.05);
            }
            else if (gamepad1.right_bumper) {
                fL.setPower(.05);
                fR.setPower(.05);
                bL.setPower(.05);
                bR.setPower(.05);
            }


            //Driver 2
            /*
            Driver 2 currently controls the arm and wrist outtake as these mechanisms should be
            operated while moving in order to reduce cycle times as much as possible. Although few
            in terms of controls, Driver 2 is still vital in watching and commanding other robots
            as well as keeping an eye out as Driver 1 will likely be focused on the main overall
            game plan while still outtaking.
//////////////////////////////////////////////////////////////////////////////////////////////////
            POSSIBLE FUTURE CHANGES:
            Test and set TRUE position for servo wrist. Possibly servo programmer.
             */

            // If the right trigger of Driver 2 is pressed past a threshold, the arm will extend out,
            // if the left trigger is pressed, the arm will retract back down. Otherwise rest.
            // Will likely combine with wrist movement and into different levels.

            // Lift Macro

            telemetry.addData("fR:", fR.getPower());
            telemetry.addData("fL:", fL.getPower());
            telemetry.addData("bR:", bR.getPower());
            telemetry.addData("bL:", bL.getPower());
            /*telemetry.addData("sArm pos:", shippingArm.getPosition());
            telemetry.addData("hArm pos:", hook.getPosition());
            telemetry.addData("sPos:", sPos);
            telemetry.addData("hPos:", hPos);*/
            telemetry.addData("State: ", liftState);
            telemetry.update();
        }
    }

    public void driveTrainPower(double forward, double strafe, double rotate){
        fL.setPower(WeightAvg(forward,strafe,-rotate));
        fR.setPower(WeightAvg(forward,-strafe,rotate));
        bL.setPower(WeightAvg(forward,-strafe,-rotate));
        bR.setPower(WeightAvg(forward,strafe,rotate));
    }
}