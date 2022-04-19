package `in`.mowito.scanningapp

import `in`.mowito.scanningapp.models.PointModel
import `in`.mowito.scanningapp.util.AppUtil
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.view.isVisible
/**
 * @author Shubhangam Garg
 * Main Activity for the application*/

class MainActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private var preview: Preview? = null
    private var captureImage: ImageCapture? = null
    private lateinit var binding: `in`.mowito.scanningapp.databinding.ActivityMainBinding

    private val requestCode = 101;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = `in`.mowito.scanningapp.databinding.ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_ScanningApp)
        setContentView(binding.root)

        //Initialization
        init()


    }
    /**
     * Initializes all UI components and Permissions
     * @return Unit
     * */
    private fun init() {
        checkAndGetPermissions()
        with(binding) {
            generatePoint.setOnClickListener {
                displayPoints()
            }

            captureImage.setOnClickListener {
                captureImage()
            }

            generatePolygon.setOnClickListener {
                drawPolygon()
            }

            rejectImage.setOnClickListener {
                removeImage()
            }

            removePoint.setOnClickListener {
                removePoints()
            }
        }
    }
    /**
     * Checks for camera permission and Requests permission if necessary
     * @return Unit*/
    private fun checkAndGetPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                requestCode
            );
        } else {
            startCamera()
        }
    }
    /**
     * Starts STREAMING from CAMERA and streams it into PREVIEW VIEW
     * @return Unit*/
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(LENS_FACING_BACK).build()

            preview = Preview.Builder().build()
            captureImage = ImageCapture.Builder().build()

            cameraProvider.unbindAll()
            preview?.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, captureImage)
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Gets random points from [AppUtil.getPointsCoordinates] and displays it over
     * Camera preview
     * @return Unit*/
    private fun displayPoints() {
        val points = AppUtil.getPointsCoordinates(getMaxCoOrdinates(), applicationContext)
        binding.pointsView.drawPoints(points)
    }
    /**
     * Gets Display size in pixels
     * @return Pair(maxX, maxY) Maximum co-ordinate of display*/
    private fun getMaxCoOrdinates(): Pair<Int, Int> {
        val display = windowManager.defaultDisplay
        val maxX = display.height
        val maxY = display.width
        return Pair(maxX, maxY)
    }
    /**
     * Draws polygon on captured Image after collecting Polygon details from [AppUtil.getPolygonDetail]
     * and also checks for the points which are inside the polygon using [AppUtil.checkForPointsInsidePolygon] method
     * @return Unit*/
    private fun drawPolygon() {
        val rawPolygonDetail = AppUtil.getPolygonDetail(getMaxCoOrdinates())
        binding.pointsView.restPointsToOriginalColor()
        val polygonDetail =
            binding.polygonView.drawPolygon(rawPolygonDetail.first, rawPolygonDetail.second)
        val randomPoints = binding.pointsView.getPoints()

        binding.pointsView.drawPoints(
            AppUtil.checkForPointsInsidePolygon(
                randomPoints,
                polygonDetail,
            )
        )

    }
    /**
     * Captures image form camera preview in form of BitMap and displays it on screen
     * @return Unit*/
    private fun captureImage() {
        val bitmap = binding.cameraPreview.bitmap;
        with(binding) {
            imageView.setImageBitmap(bitmap)
            captureImage.isEnabled = false
            generatePolygon.isVisible = true
            rejectImage.isVisible = true
            removePoint.isVisible = true
        }

    }
    /**
     * Removes image from the View and returns back Camera Feed (Preview View)
     * @return Unit*/
    private fun removeImage() {
        with(binding) {
            imageView.setImageBitmap(null)
            polygonView.removePolygon()
            pointsView.removePoints()
            captureImage.isEnabled = true
            generatePolygon.isVisible = false
            rejectImage.isVisible = false
            removePoint.isVisible = false
        }

    }
    /**
     * Removes randomly generated points from CameraFeed
     * @return Unit*/
    private fun removePoints() {
        binding.pointsView.removePoints()
    }
}