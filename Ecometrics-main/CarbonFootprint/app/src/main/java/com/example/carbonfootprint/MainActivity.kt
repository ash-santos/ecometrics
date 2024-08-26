package com.example.carbonfootprint

import android.Manifest
import android.animation.ArgbEvaluator
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.carbonfootprint.CalculationFun.EmissionLevelIdentifier
import com.example.carbonfootprint.database.FirebaseQuery
import com.example.carbonfootprint.database.LocationData
import com.example.carbonfootprint.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date


class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 100
    }

    private val polygonNames = listOf(
        listOf(/* Points for polygonAbucay */
            GeoPoint(14.705256, 120.553265),
            GeoPoint(14.705271, 120.552936),
            GeoPoint(14.705231, 120.552761),
            GeoPoint(14.705185, 120.552241),
            GeoPoint(14.705173, 120.552166),
            GeoPoint(14.704906, 120.551523),
            GeoPoint(14.704756, 120.551026),
            GeoPoint(14.704594, 120.550593),
            GeoPoint(14.704261, 120.549911),
            GeoPoint(14.703882, 120.549419),
            GeoPoint(14.703948, 120.549341),
            GeoPoint(14.704648, 120.548261),
            GeoPoint(14.702147, 120.537773),
            GeoPoint(14.702345, 120.535664),
            GeoPoint(14.702190, 120.535157),
            GeoPoint(14.701525, 120.534666),
            GeoPoint(14.701141, 120.534717),
            GeoPoint(14.701058, 120.534578),
            GeoPoint(14.700445, 120.534674),
            GeoPoint(14.700343, 120.534096),
            GeoPoint(14.699761, 120.533339),
            GeoPoint(14.699221, 120.530247),
            GeoPoint(14.699361, 120.529534),
            GeoPoint(14.699410, 120.528902),
            GeoPoint(14.699631, 120.528876),
            GeoPoint(14.699564, 120.528001),
            GeoPoint(14.699160, 120.527948),
            GeoPoint(14.698973, 120.527177),
            GeoPoint(14.699212, 120.525911),
            GeoPoint(14.698692, 120.525438),
            GeoPoint(14.699326, 120.524718),
            GeoPoint(14.699315, 120.523669),
            GeoPoint(14.699667, 120.523593),
            GeoPoint(14.699586, 120.522615),
            GeoPoint(14.699987, 120.522509),
            GeoPoint(14.699574, 120.521918),
            GeoPoint(14.699368, 120.521188),
            GeoPoint(14.699646, 120.519622),
            GeoPoint(14.699284, 120.517818),
            GeoPoint(14.698423, 120.516285),
            GeoPoint(14.698204, 120.515708),
            GeoPoint(14.697821, 120.514965),
            GeoPoint(14.697664, 120.514226),
            GeoPoint(14.697663, 120.513443),
            GeoPoint(14.697830, 120.512788),
            GeoPoint(14.696960, 120.511716),
            GeoPoint(14.696347, 120.511813),
            GeoPoint(14.695507, 120.509645),
            GeoPoint(14.694770, 120.508821),
            GeoPoint(14.694656, 120.508270),
            GeoPoint(14.695007, 120.507821),
            GeoPoint(14.695007, 120.507241),
            GeoPoint(14.694654, 120.507166),
            GeoPoint(14.694551, 120.506813),
            GeoPoint(14.694676, 120.506277),
            GeoPoint(14.693908, 120.506340),
            GeoPoint(14.693742, 120.505901),
            GeoPoint(14.694208, 120.505364),
            GeoPoint(14.693751, 120.505031),
            GeoPoint(14.693804, 120.504509),
            GeoPoint(14.693421, 120.503594),
            GeoPoint(14.693422, 120.502983),
            GeoPoint(14.692818, 120.502081),
            GeoPoint(14.692672, 120.501514),
            GeoPoint(14.692557, 120.500783),
            GeoPoint(14.692640, 120.499989),
            GeoPoint(14.691729, 120.500245),
            GeoPoint(14.691070, 120.498900),
            GeoPoint(14.691245, 120.497880),
            GeoPoint(14.691609, 120.497446),
            GeoPoint(14.691457, 120.497154),
            GeoPoint(14.691328, 120.496814),
            GeoPoint(14.690947, 120.496529),
            GeoPoint(14.691163, 120.495986),
            GeoPoint(14.690980, 120.491922),
            GeoPoint(14.689914, 120.489980),
            GeoPoint(14.689703, 120.487975),
            GeoPoint(14.689517, 120.486780),
            GeoPoint(14.688760, 120.484355),
            GeoPoint(14.687889, 120.483499),
            GeoPoint(14.697519, 120.450154),
            GeoPoint(14.695403, 120.441742),
            GeoPoint(14.695402, 120.439337),
            GeoPoint(14.694987, 120.435047),
            GeoPoint(14.700491, 120.427006),
            GeoPoint(14.716438, 120.433493),
            GeoPoint(14.727497, 120.438021),
            GeoPoint(14.728722, 120.437463),
            GeoPoint(14.736988, 120.440835),
            GeoPoint(14.743068, 120.445749),
            GeoPoint(14.746057, 120.448775),
            GeoPoint(14.748007, 120.453496),
            GeoPoint(14.747779, 120.455148),
            GeoPoint(14.748111, 120.456864),
            GeoPoint(14.748090, 120.458409),
            GeoPoint(14.749397, 120.461263),
            GeoPoint(14.749335, 120.463066),
            GeoPoint(14.748733, 120.464160),
            GeoPoint(14.748920, 120.465104),
            GeoPoint(14.752655, 120.468237),
            GeoPoint(14.751991, 120.469224),
            GeoPoint(14.751680, 120.471155),
            GeoPoint(14.753029, 120.475640),
            GeoPoint(14.752676, 120.477271),
            GeoPoint(14.752946, 120.478344),
            GeoPoint(14.753568, 120.478880),
            GeoPoint(14.751970, 120.482356),
            GeoPoint(14.753257, 120.485124),
            GeoPoint(14.753535, 120.486592),
            GeoPoint(14.754406, 120.487622),
            GeoPoint(14.754271, 120.489757),
            GeoPoint(14.755018, 120.493147),
            GeoPoint(14.754728, 120.494434),
            GeoPoint(14.755786, 120.495110),
            GeoPoint(14.756543, 120.498071),
            GeoPoint(14.756917, 120.504165),
            GeoPoint(14.755890, 120.504627),
            GeoPoint(14.756004, 120.505088),
            GeoPoint(14.756844, 120.505828),
            GeoPoint(14.756813, 120.506558),
            GeoPoint(14.756375, 120.507238),
            GeoPoint(14.756692, 120.514199),
            GeoPoint(14.756890, 120.518577),
            GeoPoint(14.754014, 120.520959),
            GeoPoint(14.752913, 120.522787),
            GeoPoint(14.750801, 120.526019),
            GeoPoint(14.749561, 120.529387),
            GeoPoint(14.748328, 120.531109),
            GeoPoint(14.747132, 120.532050),
            GeoPoint(14.744623, 120.536223),
            GeoPoint(14.742620, 120.538468),
            GeoPoint(14.742437, 120.539151),
            GeoPoint(14.742569, 120.540479),
            GeoPoint(14.742261, 120.540987),
            GeoPoint(14.742928, 120.542102),
            GeoPoint(14.742782, 120.544886),
            GeoPoint(14.742011, 120.546131),
            GeoPoint(14.740845, 120.549279),
            GeoPoint(14.739267, 120.548945),
            GeoPoint(14.737463, 120.548376),
            GeoPoint(14.734799, 120.547648),
            GeoPoint(14.733420, 120.547382),
            GeoPoint(14.731747, 120.547071),
            GeoPoint(14.729186, 120.546904),
            GeoPoint(14.726780, 120.546874),
            GeoPoint(14.725936, 120.547003),
            GeoPoint(14.723302, 120.547671),
            GeoPoint(14.722906, 120.547913),
            GeoPoint(14.722664, 120.548179),
            GeoPoint(14.722157, 120.548626),
            GeoPoint(14.721710, 120.549180),
            GeoPoint(14.721211, 120.549309),
            GeoPoint(14.720470, 120.548631),
            GeoPoint(14.713873, 120.549082),
            GeoPoint(14.711064, 120.550165),
            GeoPoint(14.710611, 120.550574),
            GeoPoint(14.710127, 120.550824),
            GeoPoint(14.709504, 120.551027),
            GeoPoint(14.706578, 120.553130)
        ),
        listOf(/* Points for polygonBagac */
            GeoPoint(14.624449, 120.356846),
            GeoPoint(14.623105, 120.358134),
            GeoPoint(14.620983, 120.357620),
            GeoPoint(14.620590, 120.358928),
            GeoPoint(14.620686, 120.359045),
            GeoPoint(14.621542, 120.358694),
            GeoPoint(14.622293, 120.359154),
            GeoPoint(14.623486, 120.359493),
            GeoPoint(14.623691, 120.359751),
            GeoPoint(14.624044, 120.361351),
            GeoPoint(14.623897, 120.361685),
            GeoPoint(14.624080, 120.362573),
            GeoPoint(14.624044, 120.362990),
            GeoPoint(14.624183, 120.363134),
            GeoPoint(14.624756, 120.363096),
            GeoPoint(14.618929, 120.368575),
            GeoPoint(14.590314, 120.388958),
            GeoPoint(14.573796, 120.384386),
            GeoPoint(14.568760, 120.382765),
            GeoPoint(14.548697, 120.380877),
            GeoPoint(14.524852, 120.374783),
            GeoPoint(14.522152, 120.374826),
            GeoPoint(14.498209, 120.377273),
            GeoPoint(14.475315, 120.381393),
            GeoPoint(14.472821, 120.382380),
            GeoPoint(14.469539, 120.385041),
            GeoPoint(14.469206, 120.393624),
            GeoPoint(14.472722, 120.405861),
            GeoPoint(14.483021, 120.424135),
            GeoPoint(14.489308, 120.437760),
            GeoPoint(14.520076, 120.468303),
            GeoPoint(14.546048, 120.478512),
            GeoPoint(14.546852, 120.478512),
            GeoPoint(14.549960, 120.478069),
            GeoPoint(14.598864, 120.479174),
            GeoPoint(14.623531, 120.478736),
            GeoPoint(14.627720, 120.476295),
            GeoPoint(14.621228, 120.473237),
            GeoPoint(14.622502, 120.472565),
            GeoPoint(14.623464, 120.472239),
            GeoPoint(14.620815, 120.471229),
            GeoPoint(14.620993, 120.465685),
            GeoPoint(14.621761, 120.460375),
            GeoPoint(14.621585, 120.458118),
            GeoPoint(14.622025, 120.458345),
            GeoPoint(14.622719, 120.458505),
            GeoPoint(14.623350, 120.458490),
            GeoPoint(14.624385, 120.458900),
            GeoPoint(14.626135, 120.459175),
            GeoPoint(14.626212, 120.460598),
            GeoPoint(14.626671, 120.461068),
            GeoPoint(14.627544, 120.460924),
            GeoPoint(14.628161, 120.460328),
            GeoPoint(14.629126, 120.461144),
            GeoPoint(14.629177, 120.461728),
            GeoPoint(14.646705, 120.445583),
            GeoPoint(14.658885, 120.453659),
            GeoPoint(14.659322, 120.454594),
            GeoPoint(14.659889, 120.450473),
            GeoPoint(14.661339, 120.446851),
            GeoPoint(14.662273, 120.443592),
            GeoPoint(14.661632, 120.441639),
            GeoPoint(14.662729, 120.440820),
            GeoPoint(14.662920, 120.439214),
            GeoPoint(14.667146, 120.435066),
            GeoPoint(14.668458, 120.431700),
            GeoPoint(14.672110, 120.429489),
            GeoPoint(14.672380, 120.428009),
            GeoPoint(14.673876, 120.426722),
            GeoPoint(14.676099, 120.425886)
        ),
        listOf(/* Points for polygonBalanga */
            GeoPoint(14.694341, 120.566982),
            GeoPoint(14.694360, 120.566975),
            GeoPoint(14.694708, 120.566709),
            GeoPoint(14.695248, 120.566225),
            GeoPoint(14.695269, 120.566185),
            GeoPoint(14.695275, 120.566148),
            GeoPoint(14.699753, 120.563139),
            GeoPoint(14.699911, 120.563063),
            GeoPoint(14.699960, 120.563045),
            GeoPoint(14.700010, 120.563014),
            GeoPoint(14.700125, 120.562847),
            GeoPoint(14.700172, 120.562820),
            GeoPoint(14.700223, 120.562806),
            GeoPoint(14.700352, 120.562784),
            GeoPoint(14.700424, 120.562766),
            GeoPoint(14.700556, 120.562717),
            GeoPoint(14.700594, 120.562643),
            GeoPoint(14.701531, 120.562094),
            GeoPoint(14.701556, 120.562082),
            GeoPoint(14.701598, 120.562018),
            GeoPoint(14.701577, 120.561993),
            GeoPoint(14.701551, 120.561965),
            GeoPoint(14.701566, 120.561937),
            GeoPoint(14.701597, 120.561914),
            GeoPoint(14.701626, 120.561895),
            GeoPoint(14.701669, 120.561858),
            GeoPoint(14.701704, 120.561820),
            GeoPoint(14.701797, 120.561734),
            GeoPoint(14.701988, 120.561563),
            GeoPoint(14.702009, 120.561538),
            GeoPoint(14.702011, 120.561519),
            GeoPoint(14.702002, 120.561503),
            GeoPoint(14.701969, 120.561492),
            GeoPoint(14.701931, 120.561482),
            GeoPoint(14.701896, 120.561460),
            GeoPoint(14.701856, 120.561428),
            GeoPoint(14.701813, 120.561380),
            GeoPoint(14.701681, 120.561177),
            GeoPoint(14.701625, 120.561067),
            GeoPoint(14.701593, 120.560935),
            GeoPoint(14.701583, 120.560816),
            GeoPoint(14.701584, 120.560671),
            GeoPoint(14.701605, 120.560530),
            GeoPoint(14.701672, 120.560047),
            GeoPoint(14.701772, 120.559346),
            GeoPoint(14.701771, 120.559290),
            GeoPoint(14.701755, 120.559219),
            GeoPoint(14.701736, 120.559180),
            GeoPoint(14.701615, 120.559010),
            GeoPoint(14.701586, 120.558950),
            GeoPoint(14.701551, 120.558883),
            GeoPoint(14.701524, 120.558797),
            GeoPoint(14.701498, 120.558722),
            GeoPoint(14.701481, 120.558642),
            GeoPoint(14.701476, 120.558614),
            GeoPoint(14.701483, 120.558552),
            GeoPoint(14.701512, 120.558418),
            GeoPoint(14.701552, 120.558329),
            GeoPoint(14.701655, 120.558158),
            GeoPoint(14.701701, 120.558076),
            GeoPoint(14.701713, 120.557994),
            GeoPoint(14.701743, 120.557816),
            GeoPoint(14.701788, 120.557756),
            GeoPoint(14.701815, 120.557728),
            GeoPoint(14.701847, 120.557696),
            GeoPoint(14.701921, 120.557649),
            GeoPoint(14.701992, 120.557611),
            GeoPoint(14.702089, 120.557581),
            GeoPoint(14.702166, 120.557564),
            GeoPoint(14.702232, 120.557544),
            GeoPoint(14.702347, 120.557506),
            GeoPoint(14.702437, 120.557465),
            GeoPoint(14.702631, 120.557305),
            GeoPoint(14.702668, 120.557274),
            GeoPoint(14.702819, 120.557109),
            GeoPoint(14.702989, 120.556933),
            GeoPoint(14.703051, 120.556880),
            GeoPoint(14.703124, 120.556747),
            GeoPoint(14.703139, 120.556691),
            GeoPoint(14.703137, 120.556649),
            GeoPoint(14.703122, 120.556595),
            GeoPoint(14.703091, 120.556530),
            GeoPoint(14.703059, 120.556464),
            GeoPoint(14.703039, 120.556403),
            GeoPoint(14.703033, 120.556338),
            GeoPoint(14.703042, 120.556253),
            GeoPoint(14.703185, 120.555806),
            GeoPoint(14.703312, 120.555532),
            GeoPoint(14.703429, 120.555326),
            GeoPoint(14.703475, 120.555211),
            GeoPoint(14.703500, 120.555093),
            GeoPoint(14.703527, 120.554802),
            GeoPoint(14.703637, 120.554489),
            GeoPoint(14.703810, 120.554140),
            GeoPoint(14.704101, 120.553860),
            GeoPoint(14.704750, 120.553465),
            GeoPoint(14.705256, 120.553265),
            GeoPoint(14.705271, 120.552936),
            GeoPoint(14.705231, 120.552761),
            GeoPoint(14.705185, 120.552241),
            GeoPoint(14.705173, 120.552166),
            GeoPoint(14.704906, 120.551523),
            GeoPoint(14.704756, 120.551026),
            GeoPoint(14.704594, 120.550593),
            GeoPoint(14.704261, 120.549911),
            GeoPoint(14.703882, 120.549419),
            GeoPoint(14.703948, 120.549341),
            GeoPoint(14.704648, 120.548261),
            GeoPoint(14.702147, 120.537773),
            GeoPoint(14.702345, 120.535664),
            GeoPoint(14.702190, 120.535157),
            GeoPoint(14.701525, 120.534666),
            GeoPoint(14.701141, 120.534717),
            GeoPoint(14.701058, 120.534578),
            GeoPoint(14.700445, 120.534674),
            GeoPoint(14.700343, 120.534096),
            GeoPoint(14.699761, 120.533339),
            GeoPoint(14.699221, 120.530247),
            GeoPoint(14.699361, 120.529534),
            GeoPoint(14.699410, 120.528902),
            GeoPoint(14.699631, 120.528876),
            GeoPoint(14.699564, 120.528001),
            GeoPoint(14.699160, 120.527948),
            GeoPoint(14.698973, 120.527177),
            GeoPoint(14.699212, 120.525911),
            GeoPoint(14.698692, 120.525438),
            GeoPoint(14.699326, 120.524718),
            GeoPoint(14.699315, 120.523669),
            GeoPoint(14.699667, 120.523593),
            GeoPoint(14.699586, 120.522615),
            GeoPoint(14.699987, 120.522509),
            GeoPoint(14.699574, 120.521918),
            GeoPoint(14.699368, 120.521188),
            GeoPoint(14.699646, 120.519622),
            GeoPoint(14.699284, 120.517818),
            GeoPoint(14.698423, 120.516285),
            GeoPoint(14.698204, 120.515708),
            GeoPoint(14.697821, 120.514965),
            GeoPoint(14.697664, 120.514226),
            GeoPoint(14.697663, 120.513443),
            GeoPoint(14.697830, 120.512788),
            GeoPoint(14.696960, 120.511716),
            GeoPoint(14.696347, 120.511813),
            GeoPoint(14.695507, 120.509645),
            GeoPoint(14.694770, 120.508821),
            GeoPoint(14.694656, 120.508270),
            GeoPoint(14.695007, 120.507821),
            GeoPoint(14.695007, 120.507241),
            GeoPoint(14.694654, 120.507166),
            GeoPoint(14.694551, 120.506813),
            GeoPoint(14.694676, 120.506277),
            GeoPoint(14.693908, 120.506340),
            GeoPoint(14.693742, 120.505901),
            GeoPoint(14.694208, 120.505364),
            GeoPoint(14.693751, 120.505031),
            GeoPoint(14.693804, 120.504509),
            GeoPoint(14.693421, 120.503594),
            GeoPoint(14.693422, 120.502983),
            GeoPoint(14.692818, 120.502081),
            GeoPoint(14.692672, 120.501514),
            GeoPoint(14.692557, 120.500783),
            GeoPoint(14.692640, 120.499989),
            GeoPoint(14.691729, 120.500245),
            GeoPoint(14.691070, 120.498900),
            GeoPoint(14.691245, 120.497880),
            GeoPoint(14.691609, 120.497446),
            GeoPoint(14.691457, 120.497154),
            GeoPoint(14.691328, 120.496814),
            GeoPoint(14.690947, 120.496529),
            GeoPoint(14.691163, 120.495986),
            GeoPoint(14.690980, 120.491922),
            GeoPoint(14.689914, 120.489980),
            GeoPoint(14.689703, 120.487975),
            GeoPoint(14.689517, 120.486780),
            GeoPoint(14.688760, 120.484355),
            GeoPoint(14.687889, 120.483499),
            GeoPoint(14.697519, 120.450154),
            GeoPoint(14.695403, 120.441742),
            GeoPoint(14.695402, 120.439337),
            GeoPoint(14.694987, 120.435047),
            GeoPoint(14.676099, 120.425886),
            GeoPoint(14.673876, 120.426722),
            GeoPoint(14.672380, 120.428009),
            GeoPoint(14.672110, 120.429489),
            GeoPoint(14.668458, 120.431700),
            GeoPoint(14.667146, 120.435066),
            GeoPoint(14.662920, 120.439214),
            GeoPoint(14.662729, 120.440820),
            GeoPoint(14.661632, 120.441639),
            GeoPoint(14.662273, 120.443592),
            GeoPoint(14.661339, 120.446851),
            GeoPoint(14.659889, 120.450473),
            GeoPoint(14.659322, 120.454594),
            GeoPoint(14.658885, 120.453659),
            GeoPoint(14.646705, 120.445583),
            GeoPoint(14.629177, 120.461728),
            GeoPoint(14.629126, 120.461144),
            GeoPoint(14.628161, 120.460328),
            GeoPoint(14.627544, 120.460924),
            GeoPoint(14.626671, 120.461068),
            GeoPoint(14.626212, 120.460598),
            GeoPoint(14.626135, 120.459175),
            GeoPoint(14.624385, 120.458900),
            GeoPoint(14.623350, 120.458490),
            GeoPoint(14.622719, 120.458505),
            GeoPoint(14.622025, 120.458345),
            GeoPoint(14.621585, 120.458118),
            GeoPoint(14.621761, 120.460375),
            GeoPoint(14.620993, 120.465685),
            GeoPoint(14.620815, 120.471229),
            GeoPoint(14.623464, 120.472239),
            GeoPoint(14.622502, 120.472565),
            GeoPoint(14.621228, 120.473237),
            GeoPoint(14.628517, 120.476564),
            GeoPoint(14.630470, 120.475991),
            GeoPoint(14.631659, 120.476605),
            GeoPoint(14.633323, 120.476878),
            GeoPoint(14.634906, 120.478465),
            GeoPoint(14.636579, 120.479470),
            GeoPoint(14.637685, 120.481005),
            GeoPoint(14.638411, 120.482750),
            GeoPoint(14.638187, 120.484146),
            GeoPoint(14.637072, 120.485242),
            GeoPoint(14.637380, 120.485720),
            GeoPoint(14.637853, 120.486430),
            GeoPoint(14.638235, 120.487674),
            GeoPoint(14.639248, 120.488668),
            GeoPoint(14.639780, 120.489673),
            GeoPoint(14.640030, 120.491274),
            GeoPoint(14.639905, 120.493170),
            GeoPoint(14.640173, 120.496966),
            GeoPoint(14.639645, 120.498506),
            GeoPoint(14.637998, 120.500665),
            GeoPoint(14.637848, 120.502828),
            GeoPoint(14.637242, 120.504831),
            GeoPoint(14.635975, 120.506546),
            GeoPoint(14.634751, 120.505962),
            GeoPoint(14.634072, 120.506626),
            GeoPoint(14.634351, 120.507074),
            GeoPoint(14.635310, 120.514928),
            GeoPoint(14.635321, 120.517311),
            GeoPoint(14.635831, 120.518707),
            GeoPoint(14.635602, 120.521198),
            GeoPoint(14.636336, 120.523216),
            GeoPoint(14.637278, 120.524420),
            GeoPoint(14.638610, 120.526567),
            GeoPoint(14.639043, 120.530023),
            GeoPoint(14.639528, 120.530876),
            GeoPoint(14.641278, 120.531840),
            GeoPoint(14.643102, 120.532139),
            GeoPoint(14.643932, 120.534116),
            GeoPoint(14.643025, 120.535189),
            GeoPoint(14.644471, 120.536335),
            GeoPoint(14.648067, 120.536603),
            GeoPoint(14.648974, 120.536395),
            GeoPoint(14.650284, 120.534881),
            GeoPoint(14.651154, 120.534650),
            GeoPoint(14.651921, 120.535374),
            GeoPoint(14.652192, 120.536520),
            GeoPoint(14.653051, 120.537665),
            GeoPoint(14.654812, 120.537165),
            GeoPoint(14.656592, 120.537104),
            GeoPoint(14.658240, 120.536338),
            GeoPoint(14.660512, 120.537654),
            GeoPoint(14.661176, 120.539278),
            GeoPoint(14.660508, 120.540723),
            GeoPoint(14.661891, 120.543867),
            GeoPoint(14.662229, 120.546492),
            GeoPoint(14.662948, 120.547604),
            GeoPoint(14.665957, 120.549075),
            GeoPoint(14.667333, 120.549572),
            GeoPoint(14.668425, 120.549194),
            GeoPoint(14.670898, 120.547281),
            GeoPoint(14.672862, 120.546363),
            GeoPoint(14.675708, 120.543787),
            GeoPoint(14.676949, 120.544239),
            GeoPoint(14.677857, 120.545130),
            GeoPoint(14.680566, 120.548532),
            GeoPoint(14.681801, 120.549429),
            GeoPoint(14.685754, 120.550080),
            GeoPoint(14.686902, 120.557306),
            GeoPoint(14.689441, 120.559222),
            GeoPoint(14.690293, 120.560098),
            GeoPoint(14.691180, 120.561167),
            GeoPoint(14.692046, 120.562453),
            GeoPoint(14.692801, 120.563941)
        ),
        listOf(/* Points for polygonDinalupihan */
            GeoPoint(14.889524, 120.478690),
            GeoPoint(14.888842, 120.475164),
            GeoPoint(14.886154, 120.475394),
            GeoPoint(14.884353, 120.475318),
            GeoPoint(14.883022, 120.474246),
            GeoPoint(14.881813, 120.473812),
            GeoPoint(14.880654, 120.474169),
            GeoPoint(14.879865, 120.474858),
            GeoPoint(14.878459, 120.475037),
            GeoPoint(14.875130, 120.473659),
            GeoPoint(14.872170, 120.473506),
            GeoPoint(14.869778, 120.476236),
            GeoPoint(14.867016, 120.476976),
            GeoPoint(14.863341, 120.475853),
            GeoPoint(14.860801, 120.477206),
            GeoPoint(14.861392, 120.479834),
            GeoPoint(14.860751, 120.480498),
            GeoPoint(14.859740, 120.480498),
            GeoPoint(14.857915, 120.481391),
            GeoPoint(14.857530, 120.483561),
            GeoPoint(14.851324, 120.490998),
            GeoPoint(14.850102, 120.490899),
            GeoPoint(14.848137, 120.492238),
            GeoPoint(14.846771, 120.492957),
            GeoPoint(14.844758, 120.493700),
            GeoPoint(14.842625, 120.492857),
            GeoPoint(14.841403, 120.490651),
            GeoPoint(14.841619, 120.488792),
            GeoPoint(14.842553, 120.488197),
            GeoPoint(14.843128, 120.486982),
            GeoPoint(14.843751, 120.486412),
            GeoPoint(14.844518, 120.487031),
            GeoPoint(14.845405, 120.486932),
            GeoPoint(14.846028, 120.485569),
            GeoPoint(14.846939, 120.485544),
            GeoPoint(14.847681, 120.485023),
            GeoPoint(14.848185, 120.483610),
            GeoPoint(14.849287, 120.482395),
            GeoPoint(14.849431, 120.481280),
            GeoPoint(14.853864, 120.471438),
            GeoPoint(14.856548, 120.470446),
            GeoPoint(14.856763, 120.470659),
            GeoPoint(14.858778, 120.468449),
            GeoPoint(14.860592, 120.466969),
            GeoPoint(14.861902, 120.467073),
            GeoPoint(14.862063, 120.466677),
            GeoPoint(14.860592, 120.465635),
            GeoPoint(14.859967, 120.464217),
            GeoPoint(14.860956, 120.462626),
            GeoPoint(14.860429, 120.461932),
            GeoPoint(14.858177, 120.461684),
            GeoPoint(14.857841, 120.461188),
            GeoPoint(14.856931, 120.461337),
            GeoPoint(14.856116, 120.461609),
            GeoPoint(14.855589, 120.462080),
            GeoPoint(14.853839, 120.460742),
            GeoPoint(14.852881, 120.459800),
            GeoPoint(14.853456, 120.457072),
            GeoPoint(14.852857, 120.456601),
            GeoPoint(14.853791, 120.455213),
            GeoPoint(14.853144, 120.454643),
            GeoPoint(14.851299, 120.445272),
            GeoPoint(14.855397, 120.445470),
            GeoPoint(14.856863, 120.442704),
            GeoPoint(14.859044, 120.441663),
            GeoPoint(14.857103, 120.439060),
            GeoPoint(14.855162, 120.437548),
            GeoPoint(14.854898, 120.436804),
            GeoPoint(14.856073, 120.435912),
            GeoPoint(14.855761, 120.435267),
            GeoPoint(14.854659, 120.434622),
            GeoPoint(14.852718, 120.432540),
            GeoPoint(14.850537, 120.432862),
            GeoPoint(14.847877, 120.432738),
            GeoPoint(14.848261, 120.431077),
            GeoPoint(14.844906, 120.429863),
            GeoPoint(14.844714, 120.428425),
            GeoPoint(14.846032, 120.426962),
            GeoPoint(14.844162, 120.421671),
            GeoPoint(14.844775, 120.420506),
            GeoPoint(14.844989, 120.416541),
            GeoPoint(14.844290, 120.415509),
            GeoPoint(14.846385, 120.414079),
            GeoPoint(14.846314, 120.413401),
            GeoPoint(14.844604, 120.411382),
            GeoPoint(14.844020, 120.407623),
            GeoPoint(14.843321, 120.406045),
            GeoPoint(14.845217, 120.404085),
            GeoPoint(14.845829, 120.399147),
            GeoPoint(14.846941, 120.397672),
            GeoPoint(14.846841, 120.397039),
            GeoPoint(14.848223, 120.390995),
            GeoPoint(14.847867, 120.390184),
            GeoPoint(14.845658, 120.389919),
            GeoPoint(14.844704, 120.389034),
            GeoPoint(14.844832, 120.387663),
            GeoPoint(14.846599, 120.385894),
            GeoPoint(14.847682, 120.386042),
            GeoPoint(14.850090, 120.381605),
            GeoPoint(14.852042, 120.381000),
            GeoPoint(14.850574, 120.372908),
            GeoPoint(14.847468, 120.366377),
            GeoPoint(14.845958, 120.363709),
            GeoPoint(14.844347, 120.358358),
            GeoPoint(14.847102, 120.347732),
            GeoPoint(14.848728, 120.348563),
            GeoPoint(14.851798, 120.352211),
            GeoPoint(14.853724, 120.351918),
            GeoPoint(14.855154, 120.351127),
            GeoPoint(14.856906, 120.350579),
            GeoPoint(14.858570, 120.350681),
            GeoPoint(14.859828, 120.351944),
            GeoPoint(14.859828, 120.353437),
            GeoPoint(14.859372, 120.355044),
            GeoPoint(14.860383, 120.354840),
            GeoPoint(14.861493, 120.354151),
            GeoPoint(14.867375, 120.352862),
            GeoPoint(14.867388, 120.354457),
            GeoPoint(14.868041, 120.356358),
            GeoPoint(14.867425, 120.358170),
            GeoPoint(14.868276, 120.360773),
            GeoPoint(14.870434, 120.361526),
            GeoPoint(14.877819, 120.352719),
            GeoPoint(14.881543, 120.353434),
            GeoPoint(14.886525, 120.353408),
            GeoPoint(14.888793, 120.358103),
            GeoPoint(14.886493, 120.362257),
            GeoPoint(14.882934, 120.361871),
            GeoPoint(14.879528, 120.362825),
            GeoPoint(14.879353, 120.363917),
            GeoPoint(14.881725, 120.366599),
            GeoPoint(14.884318, 120.367099),
            GeoPoint(14.886493, 120.371010),
            GeoPoint(14.886515, 120.373215),
            GeoPoint(14.885680, 120.374988),
            GeoPoint(14.883813, 120.377693),
            GeoPoint(14.883329, 120.383377),
            GeoPoint(14.885263, 120.385423),
            GeoPoint(14.886779, 120.386196),
            GeoPoint(14.888514, 120.386377),
            GeoPoint(14.890777, 120.385309),
            GeoPoint(14.892096, 120.383831),
            GeoPoint(14.893568, 120.383195),
            GeoPoint(14.896709, 120.383900),
            GeoPoint(14.900224, 120.382217),
            GeoPoint(14.902355, 120.383218),
            GeoPoint(14.903915, 120.384877),
            GeoPoint(14.903058, 120.387855),
            GeoPoint(14.903322, 120.389469),
            GeoPoint(14.902795, 120.392152),
            GeoPoint(14.904794, 120.395198),
            GeoPoint(14.906705, 120.395994),
            GeoPoint(14.907738, 120.398131),
            GeoPoint(14.901147, 120.409634),
            GeoPoint(14.899697, 120.418159),
            GeoPoint(14.900774, 120.421683),
            GeoPoint(14.901546, 120.427103),
            GeoPoint(14.903919, 120.428876),
            GeoPoint(14.905325, 120.433423),
            GeoPoint(14.902117, 120.436969),
            GeoPoint(14.902864, 120.442016),
            GeoPoint(14.911696, 120.446336),
            GeoPoint(14.918462, 120.445017),
            GeoPoint(14.917978, 120.449973),
            GeoPoint(14.915254, 120.457930),
            GeoPoint(14.913980, 120.478208),
            GeoPoint(14.912486, 120.482755),
            GeoPoint(14.909411, 120.484665),
            GeoPoint(14.908444, 120.485983),
            GeoPoint(14.907038, 120.485165),
            GeoPoint(14.904051, 120.486301),
            GeoPoint(14.898998, 120.479936),
            GeoPoint(14.895702, 120.478299)
        ),
        listOf(/* Points for polygonHermosa */
            GeoPoint(14.744091, 120.394787),
            GeoPoint(14.758469, 120.414382),
            GeoPoint(14.762579, 120.424005),
            GeoPoint(14.765940, 120.427438),
            GeoPoint(14.767725, 120.431429),
            GeoPoint(14.787994, 120.443625),
            GeoPoint(14.794177, 120.446071),
            GeoPoint(14.797745, 120.448990),
            GeoPoint(14.799239, 120.450620),
            GeoPoint(14.799986, 120.452294),
            GeoPoint(14.803181, 120.453539),
            GeoPoint(14.806209, 120.456543),
            GeoPoint(14.817038, 120.460534),
            GeoPoint(14.818407, 120.461349),
            GeoPoint(14.818822, 120.463538),
            GeoPoint(14.818075, 120.466199),
            GeoPoint(14.807579, 120.478987),
            GeoPoint(14.807205, 120.481219),
            GeoPoint(14.807330, 120.484094),
            GeoPoint(14.807993, 120.485768),
            GeoPoint(14.810441, 120.490617),
            GeoPoint(14.813138, 120.500102),
            GeoPoint(14.820357, 120.515680),
            GeoPoint(14.822307, 120.518126),
            GeoPoint(14.823012, 120.519671),
            GeoPoint(14.822888, 120.521688),
            GeoPoint(14.814302, 120.540840),
            GeoPoint(14.814365, 120.541984),
            GeoPoint(14.813925, 120.542105),
            GeoPoint(14.812862, 120.541825),
            GeoPoint(14.812194, 120.541681),
            GeoPoint(14.811725, 120.541696),
            GeoPoint(14.805989, 120.542174),
            GeoPoint(14.803884, 120.542955),
            GeoPoint(14.803496, 120.543494),
            GeoPoint(14.803048, 120.543903),
            GeoPoint(14.802784, 120.544298),
            GeoPoint(14.801904, 120.544814),
            GeoPoint(14.800980, 120.545512),
            GeoPoint(14.800276, 120.545838),
            GeoPoint(14.800026, 120.546005),
            GeoPoint(14.797811, 120.548091),
            GeoPoint(14.797885, 120.548425),
            GeoPoint(14.796044, 120.550018),
            GeoPoint(14.795325, 120.550815),
            GeoPoint(14.795207, 120.550822),
            GeoPoint(14.795853, 120.551513),
            GeoPoint(14.795963, 120.551725),
            GeoPoint(14.796007, 120.551907),
            GeoPoint(14.796872, 120.553311),
            GeoPoint(14.797393, 120.553265),
            GeoPoint(14.798090, 120.553387),
            GeoPoint(14.799909, 120.553409),
            GeoPoint(14.799990, 120.553500),
            GeoPoint(14.799946, 120.554282),
            GeoPoint(14.799733, 120.554304),
            GeoPoint(14.799579, 120.556171),
            GeoPoint(14.803342, 120.556342),
            GeoPoint(14.812972, 120.557928),
            GeoPoint(14.813192, 120.557905),
            GeoPoint(14.813361, 120.557632),
            GeoPoint(14.820141, 120.558167),
            GeoPoint(14.823237, 120.557758),
            GeoPoint(14.824088, 120.558364),
            GeoPoint(14.825852, 120.558970),
            GeoPoint(14.827480, 120.559774),
            GeoPoint(14.828115, 120.560297),
            GeoPoint(14.829071, 120.561242),
            GeoPoint(14.831180, 120.562262),
            GeoPoint(14.832130, 120.563219),
            GeoPoint(14.832222, 120.563449),
            GeoPoint(14.832438, 120.563717),
            GeoPoint(14.832894, 120.563940),
            GeoPoint(14.833949, 120.564170),
            GeoPoint(14.835706, 120.563915),
            GeoPoint(14.839530, 120.564776),
            GeoPoint(14.840001, 120.565103),
            GeoPoint(14.840045, 120.565414),
            GeoPoint(14.840331, 120.565664),
            GeoPoint(14.842678, 120.566666),
            GeoPoint(14.843044, 120.566347),
            GeoPoint(14.843125, 120.566150),
            GeoPoint(14.843147, 120.565353),
            GeoPoint(14.843389, 120.564936),
            GeoPoint(14.843822, 120.564648),
            GeoPoint(14.844130, 120.564580),
            GeoPoint(14.844958, 120.564671),
            GeoPoint(14.846176, 120.564928),
            GeoPoint(14.846660, 120.564928),
            GeoPoint(14.848735, 120.564617),
            GeoPoint(14.852687, 120.562410),
            GeoPoint(14.853083, 120.560490),
            GeoPoint(14.853039, 120.560255),
            GeoPoint(14.849913, 120.554909),
            GeoPoint(14.849247, 120.553199),
            GeoPoint(14.849642, 120.546003),
            GeoPoint(14.851023, 120.542813),
            GeoPoint(14.848534, 120.539575),
            GeoPoint(14.842565, 120.537228),
            GeoPoint(14.840641, 120.535544),
            GeoPoint(14.841529, 120.532635),
            GeoPoint(14.843848, 120.524367),
            GeoPoint(14.846364, 120.524265),
            GeoPoint(14.854405, 120.520846),
            GeoPoint(14.858746, 120.517375),
            GeoPoint(14.865158, 120.500023),
            GeoPoint(14.876602, 120.496042),
            GeoPoint(14.879315, 120.493695),
            GeoPoint(14.879117, 120.490990),
            GeoPoint(14.883507, 120.483590),
            GeoPoint(14.889524, 120.478690),
            GeoPoint(14.888842, 120.475164),
            GeoPoint(14.886154, 120.475394),
            GeoPoint(14.884353, 120.475318),
            GeoPoint(14.883022, 120.474246),
            GeoPoint(14.881813, 120.473812),
            GeoPoint(14.880654, 120.474169),
            GeoPoint(14.879865, 120.474858),
            GeoPoint(14.878459, 120.475037),
            GeoPoint(14.875130, 120.473659),
            GeoPoint(14.872170, 120.473506),
            GeoPoint(14.869778, 120.476236),
            GeoPoint(14.867016, 120.476976),
            GeoPoint(14.863341, 120.475853),
            GeoPoint(14.860801, 120.477206),
            GeoPoint(14.861392, 120.479834),
            GeoPoint(14.860751, 120.480498),
            GeoPoint(14.859740, 120.480498),
            GeoPoint(14.857915, 120.481391),
            GeoPoint(14.857530, 120.483561),
            GeoPoint(14.851324, 120.490998),
            GeoPoint(14.850102, 120.490899),
            GeoPoint(14.848137, 120.492238),
            GeoPoint(14.846771, 120.492957),
            GeoPoint(14.844758, 120.493700),
            GeoPoint(14.842625, 120.492857),
            GeoPoint(14.841403, 120.490651),
            GeoPoint(14.841619, 120.488792),
            GeoPoint(14.842553, 120.488197),
            GeoPoint(14.843128, 120.486982),
            GeoPoint(14.843751, 120.486412),
            GeoPoint(14.844518, 120.487031),
            GeoPoint(14.845405, 120.486932),
            GeoPoint(14.846028, 120.485569),
            GeoPoint(14.846939, 120.485544),
            GeoPoint(14.847681, 120.485023),
            GeoPoint(14.848185, 120.483610),
            GeoPoint(14.849287, 120.482395),
            GeoPoint(14.849431, 120.481280),
            GeoPoint(14.853864, 120.471438),
            GeoPoint(14.856548, 120.470446),
            GeoPoint(14.856763, 120.470659),
            GeoPoint(14.858778, 120.468449),
            GeoPoint(14.860592, 120.466969),
            GeoPoint(14.861902, 120.467073),
            GeoPoint(14.862063, 120.466677),
            GeoPoint(14.860592, 120.465635),
            GeoPoint(14.859967, 120.464217),
            GeoPoint(14.860956, 120.462626),
            GeoPoint(14.860429, 120.461932),
            GeoPoint(14.858177, 120.461684),
            GeoPoint(14.857841, 120.461188),
            GeoPoint(14.856931, 120.461337),
            GeoPoint(14.856116, 120.461609),
            GeoPoint(14.855589, 120.462080),
            GeoPoint(14.853839, 120.460742),
            GeoPoint(14.852881, 120.459800),
            GeoPoint(14.853456, 120.457072),
            GeoPoint(14.852857, 120.456601),
            GeoPoint(14.853791, 120.455213),
            GeoPoint(14.853144, 120.454643),
            GeoPoint(14.851299, 120.445272),
            GeoPoint(14.855397, 120.445470),
            GeoPoint(14.856863, 120.442704),
            GeoPoint(14.859044, 120.441663),
            GeoPoint(14.857103, 120.439060),
            GeoPoint(14.855162, 120.437548),
            GeoPoint(14.854898, 120.436804),
            GeoPoint(14.856073, 120.435912),
            GeoPoint(14.855761, 120.435267),
            GeoPoint(14.854659, 120.434622),
            GeoPoint(14.852718, 120.432540),
            GeoPoint(14.850537, 120.432862),
            GeoPoint(14.847877, 120.432738),
            GeoPoint(14.848261, 120.431077),
            GeoPoint(14.844906, 120.429863),
            GeoPoint(14.844714, 120.428425),
            GeoPoint(14.846032, 120.426962),
            GeoPoint(14.844162, 120.421671),
            GeoPoint(14.844775, 120.420506),
            GeoPoint(14.844989, 120.416541),
            GeoPoint(14.844290, 120.415509),
            GeoPoint(14.846385, 120.414079),
            GeoPoint(14.846314, 120.413401),
            GeoPoint(14.844604, 120.411382),
            GeoPoint(14.844020, 120.407623),
            GeoPoint(14.843321, 120.406045),
            GeoPoint(14.845217, 120.404085),
            GeoPoint(14.845829, 120.399147),
            GeoPoint(14.846941, 120.397672),
            GeoPoint(14.846841, 120.397039),
            GeoPoint(14.848223, 120.390995),
            GeoPoint(14.847867, 120.390184),
            GeoPoint(14.845658, 120.389919),
            GeoPoint(14.844704, 120.389034),
            GeoPoint(14.844832, 120.387663),
            GeoPoint(14.846599, 120.385894),
            GeoPoint(14.847682, 120.386042),
            GeoPoint(14.850090, 120.381605),
            GeoPoint(14.852042, 120.381000),
            GeoPoint(14.850574, 120.372908),
            GeoPoint(14.847468, 120.366377),
            GeoPoint(14.845958, 120.363709),
            GeoPoint(14.844347, 120.358358)
        ),
        listOf(/* Points for polygonLimay */
            GeoPoint(14.549960, 120.478069),
            GeoPoint(14.556409, 120.498225),
            GeoPoint(14.568461, 120.537573),
            GeoPoint(14.569202, 120.548648),
            GeoPoint(14.569449, 120.573145),
            GeoPoint(14.570067, 120.577917),
            GeoPoint(14.571277, 120.583556),
            GeoPoint(14.571622, 120.589042),
            GeoPoint(14.571647, 120.590318),
            GeoPoint(14.572956, 120.594299),
            GeoPoint(14.572140, 120.594392),
            GeoPoint(14.572112, 120.594392),
            GeoPoint(14.571621, 120.594548),
            GeoPoint(14.570794, 120.594847),
            GeoPoint(14.570609, 120.594946),
            GeoPoint(14.570067, 120.595174),
            GeoPoint(14.569851, 120.595291),
            GeoPoint(14.569712, 120.595405),
            GeoPoint(14.569654, 120.595489),
            GeoPoint(14.569634, 120.595549),
            GeoPoint(14.569602, 120.595712),
            GeoPoint(14.569491, 120.595951),
            GeoPoint(14.569301, 120.596226),
            GeoPoint(14.569152, 120.596427),
            GeoPoint(14.568773, 120.596647),
            GeoPoint(14.568542, 120.596717),
            GeoPoint(14.567597, 120.596848),
            GeoPoint(14.566907, 120.596840),
            GeoPoint(14.566401, 120.596883),
            GeoPoint(14.566032, 120.596910),
            GeoPoint(14.565671, 120.596983),
            GeoPoint(14.564822, 120.597326),
            GeoPoint(14.564705, 120.597401),
            GeoPoint(14.564539, 120.597578),
            GeoPoint(14.564389, 120.598005),
            GeoPoint(14.564358, 120.598080),
            GeoPoint(14.560362, 120.600005),
            GeoPoint(14.559365, 120.599517),
            GeoPoint(14.558031, 120.599403),
            GeoPoint(14.557880, 120.599365),
            GeoPoint(14.557699, 120.599250),
            GeoPoint(14.556941, 120.598918),
            GeoPoint(14.556559, 120.598850),
            GeoPoint(14.556276, 120.598842),
            GeoPoint(14.555990, 120.598826),
            GeoPoint(14.555653, 120.598743),
            GeoPoint(14.553869, 120.598754),
            GeoPoint(14.552854, 120.598813),
            GeoPoint(14.552060, 120.598864),
            GeoPoint(14.551403, 120.598869),
            GeoPoint(14.551053, 120.598907),
            GeoPoint(14.549983, 120.599065),
            GeoPoint(14.548760, 120.599424),
            GeoPoint(14.548607, 120.599596),
            GeoPoint(14.548610, 120.599795),
            GeoPoint(14.548571, 120.599889),
            GeoPoint(14.547937, 120.600052),
            GeoPoint(14.547859, 120.599639),
            GeoPoint(14.547698, 120.599658),
            GeoPoint(14.547686, 120.599767),
            GeoPoint(14.547675, 120.599805),
            GeoPoint(14.547652, 120.599838),
            GeoPoint(14.547613, 120.599869),
            GeoPoint(14.547520, 120.599907),
            GeoPoint(14.547359, 120.599948),
            GeoPoint(14.547257, 120.599960),
            GeoPoint(14.547115, 120.599913),
            GeoPoint(14.546991, 120.599895),
            GeoPoint(14.546865, 120.599919),
            GeoPoint(14.546736, 120.599959),
            GeoPoint(14.546199, 120.600264),
            GeoPoint(14.546077, 120.600353),
            GeoPoint(14.545988, 120.600441),
            GeoPoint(14.545896, 120.600571),
            GeoPoint(14.545785, 120.600677),
            GeoPoint(14.545518, 120.600885),
            GeoPoint(14.545427, 120.600994),
            GeoPoint(14.545358, 120.601114),
            GeoPoint(14.545301, 120.601275),
            GeoPoint(14.545271, 120.601471),
            GeoPoint(14.545258, 120.601719),
            GeoPoint(14.545269, 120.601876),
            GeoPoint(14.545264, 120.602161),
            GeoPoint(14.545252, 120.602265),
            GeoPoint(14.545208, 120.602356),
            GeoPoint(14.545166, 120.602388),
            GeoPoint(14.545117, 120.602411),
            GeoPoint(14.545063, 120.602460),
            GeoPoint(14.545025, 120.602514),
            GeoPoint(14.544998, 120.602616),
            GeoPoint(14.544999, 120.602741),
            GeoPoint(14.545017, 120.602814),
            GeoPoint(14.545066, 120.602921),
            GeoPoint(14.545074, 120.602983),
            GeoPoint(14.545065, 120.603007),
            GeoPoint(14.545001, 120.603122),
            GeoPoint(14.544969, 120.603125),
            GeoPoint(14.544861, 120.603089),
            GeoPoint(14.544817, 120.603062),
            GeoPoint(14.544725, 120.602961),
            GeoPoint(14.544614, 120.602905),
            GeoPoint(14.544416, 120.602830),
            GeoPoint(14.544300, 120.602755),
            GeoPoint(14.543967, 120.602502),
            GeoPoint(14.543598, 120.602299),
            GeoPoint(14.543363, 120.602215),
            GeoPoint(14.543005, 120.602146),
            GeoPoint(14.542791, 120.602126),
            GeoPoint(14.542456, 120.602068),
            GeoPoint(14.542157, 120.602005),
            GeoPoint(14.541846, 120.601925),
            GeoPoint(14.541610, 120.601827),
            GeoPoint(14.541498, 120.601741),
            GeoPoint(14.541395, 120.601605),
            GeoPoint(14.541130, 120.601102),
            GeoPoint(14.541035, 120.600780),
            GeoPoint(14.541003, 120.600719),
            GeoPoint(14.540948, 120.600652),
            GeoPoint(14.540839, 120.600580),
            GeoPoint(14.540687, 120.600537),
            GeoPoint(14.538810, 120.600558),
            GeoPoint(14.538665, 120.600587),
            GeoPoint(14.538518, 120.600654),
            GeoPoint(14.538363, 120.600799),
            GeoPoint(14.538054, 120.601136),
            GeoPoint(14.537946, 120.601311),
            GeoPoint(14.537901, 120.601463),
            GeoPoint(14.537900, 120.601579),
            GeoPoint(14.537915, 120.601739),
            GeoPoint(14.537914, 120.601822),
            GeoPoint(14.537872, 120.602043),
            GeoPoint(14.537826, 120.602154),
            GeoPoint(14.537689, 120.602419),
            GeoPoint(14.537522, 120.602620),
            GeoPoint(14.537313, 120.602759),
            GeoPoint(14.537172, 120.602800),
            GeoPoint(14.536756, 120.602900),
            GeoPoint(14.536556, 120.603001),
            GeoPoint(14.536282, 120.603163),
            GeoPoint(14.536204, 120.603224),
            GeoPoint(14.536152, 120.603305),
            GeoPoint(14.536124, 120.603427),
            GeoPoint(14.536107, 120.603599),
            GeoPoint(14.536104, 120.603800),
            GeoPoint(14.536103, 120.604321),
            GeoPoint(14.536093, 120.604443),
            GeoPoint(14.536043, 120.604573),
            GeoPoint(14.535970, 120.604678),
            GeoPoint(14.535857, 120.604770),
            GeoPoint(14.535048, 120.605179),
            GeoPoint(14.534424, 120.605572),
            GeoPoint(14.534281, 120.605698),
            GeoPoint(14.534205, 120.605808),
            GeoPoint(14.534155, 120.605946),
            GeoPoint(14.534124, 120.606113),
            GeoPoint(14.534128, 120.606264),
            GeoPoint(14.534118, 120.606372),
            GeoPoint(14.534068, 120.606469),
            GeoPoint(14.534017, 120.606535),
            GeoPoint(14.533858, 120.606691),
            GeoPoint(14.533803, 120.606889),
            GeoPoint(14.533795, 120.607000),
            GeoPoint(14.533808, 120.607320),
            GeoPoint(14.533799, 120.607446),
            GeoPoint(14.533745, 120.607788),
            GeoPoint(14.533742, 120.608430),
            GeoPoint(14.531408, 120.608169),
            GeoPoint(14.531318, 120.607106),
            GeoPoint(14.531186, 120.607019),
            GeoPoint(14.531089, 120.606979),
            GeoPoint(14.531010, 120.606979),
            GeoPoint(14.530770, 120.607039),
            GeoPoint(14.530686, 120.607060),
            GeoPoint(14.530512, 120.607062),
            GeoPoint(14.530416, 120.607038),
            GeoPoint(14.530368, 120.607024),
            GeoPoint(14.530308, 120.606988),
            GeoPoint(14.530251, 120.606930),
            GeoPoint(14.530153, 120.606667),
            GeoPoint(14.530111, 120.606608),
            GeoPoint(14.530067, 120.606517),
            GeoPoint(14.530002, 120.606478),
            GeoPoint(14.529949, 120.606429),
            GeoPoint(14.529899, 120.606283),
            GeoPoint(14.529875, 120.606236),
            GeoPoint(14.529856, 120.606170),
            GeoPoint(14.529825, 120.606116),
            GeoPoint(14.529783, 120.606075),
            GeoPoint(14.529724, 120.606033),
            GeoPoint(14.529660, 120.606010),
            GeoPoint(14.529467, 120.605963),
            GeoPoint(14.529169, 120.605971),
            GeoPoint(14.529105, 120.605977),
            GeoPoint(14.529057, 120.605990),
            GeoPoint(14.528996, 120.606017),
            GeoPoint(14.528747, 120.606146),
            GeoPoint(14.528707, 120.606181),
            GeoPoint(14.528656, 120.606242),
            GeoPoint(14.528645, 120.606299),
            GeoPoint(14.528654, 120.606341),
            GeoPoint(14.528733, 120.606409),
            GeoPoint(14.528830, 120.606530),
            GeoPoint(14.528857, 120.606616),
            GeoPoint(14.528860, 120.606676),
            GeoPoint(14.528874, 120.606720),
            GeoPoint(14.528868, 120.606923),
            GeoPoint(14.528860, 120.606986),
            GeoPoint(14.528858, 120.607060),
            GeoPoint(14.528847, 120.607173),
            GeoPoint(14.528767, 120.607394),
            GeoPoint(14.528700, 120.607508),
            GeoPoint(14.528629, 120.607602),
            GeoPoint(14.528539, 120.607694),
            GeoPoint(14.528394, 120.607840),
            GeoPoint(14.528328, 120.607879),
            GeoPoint(14.528269, 120.607911),
            GeoPoint(14.528189, 120.607946),
            GeoPoint(14.528040, 120.607997),
            GeoPoint(14.527955, 120.608018),
            GeoPoint(14.527795, 120.608066),
            GeoPoint(14.527557, 120.608159),
            GeoPoint(14.527467, 120.608176),
            GeoPoint(14.527316, 120.608233),
            GeoPoint(14.527095, 120.608297),
            GeoPoint(14.526989, 120.608332),
            GeoPoint(14.526762, 120.608382),
            GeoPoint(14.526517, 120.608417),
            GeoPoint(14.526095, 120.608351),
            GeoPoint(14.526029, 120.608328),
            GeoPoint(14.525891, 120.608275),
            GeoPoint(14.525744, 120.608199),
            GeoPoint(14.525687, 120.608153),
            GeoPoint(14.525669, 120.608128),
            GeoPoint(14.525630, 120.608096),
            GeoPoint(14.525535, 120.608067),
            GeoPoint(14.525455, 120.607994),
            GeoPoint(14.525396, 120.607961),
            GeoPoint(14.525345, 120.607955),
            GeoPoint(14.525238, 120.607963),
            GeoPoint(14.524824, 120.608076),
            GeoPoint(14.524517, 120.608213),
            GeoPoint(14.524410, 120.608271),
            GeoPoint(14.524329, 120.608384),
            GeoPoint(14.524267, 120.608502),
            GeoPoint(14.524192, 120.608594),
            GeoPoint(14.524107, 120.608641),
            GeoPoint(14.524047, 120.608664),
            GeoPoint(14.523984, 120.608678),
            GeoPoint(14.523831, 120.608674),
            GeoPoint(14.523675, 120.608688),
            GeoPoint(14.523121, 120.608764),
            GeoPoint(14.522830, 120.608830),
            GeoPoint(14.522006, 120.609104),
            GeoPoint(14.521954, 120.609144),
            GeoPoint(14.521913, 120.609180),
            GeoPoint(14.521820, 120.609426),
            GeoPoint(14.521752, 120.609516),
            GeoPoint(14.521699, 120.609722),
            GeoPoint(14.521722, 120.609865),
            GeoPoint(14.521712, 120.609954),
            GeoPoint(14.521698, 120.610028),
            GeoPoint(14.521646, 120.610105),
            GeoPoint(14.521583, 120.610155),
            GeoPoint(14.521516, 120.610195),
            GeoPoint(14.521446, 120.610201),
            GeoPoint(14.521375, 120.610192),
            GeoPoint(14.521298, 120.610172),
            GeoPoint(14.521261, 120.610113),
            GeoPoint(14.521228, 120.609960),
            GeoPoint(14.521222, 120.609873),
            GeoPoint(14.521207, 120.609796),
            GeoPoint(14.521195, 120.609637),
            GeoPoint(14.521188, 120.609567),
            GeoPoint(14.521155, 120.609509),
            GeoPoint(14.521112, 120.609469),
            GeoPoint(14.521044, 120.609447),
            GeoPoint(14.520948, 120.609467),
            GeoPoint(14.520832, 120.609548),
            GeoPoint(14.520739, 120.609553),
            GeoPoint(14.520245, 120.609562),
            GeoPoint(14.520168, 120.609554),
            GeoPoint(14.520097, 120.609536),
            GeoPoint(14.520036, 120.609498),
            GeoPoint(14.519981, 120.609453),
            GeoPoint(14.519925, 120.609385),
            GeoPoint(14.519916, 120.609335),
            GeoPoint(14.519944, 120.608651),
            GeoPoint(14.519938, 120.608548),
            GeoPoint(14.519930, 120.608514),
            GeoPoint(14.519913, 120.608480),
            GeoPoint(14.519886, 120.608457),
            GeoPoint(14.519742, 120.608423),
            GeoPoint(14.518467, 120.608485),
            GeoPoint(14.517925, 120.608562),
            GeoPoint(14.517272, 120.608805),
            GeoPoint(14.515632, 120.609360),
            GeoPoint(14.514753, 120.609669),
            GeoPoint(14.514522, 120.609838),
            GeoPoint(14.514335, 120.609980),
            GeoPoint(14.514202, 120.610061),
            GeoPoint(14.514074, 120.610087),
            GeoPoint(14.514037, 120.610119),
            GeoPoint(14.514023, 120.610195),
            GeoPoint(14.514256, 120.611257),
            GeoPoint(14.514254, 120.611364),
            GeoPoint(14.514408, 120.612040),
            GeoPoint(14.514298, 120.612072),
            GeoPoint(14.514195, 120.611604),
            GeoPoint(14.514113, 120.611626),
            GeoPoint(14.514096, 120.611541),
            GeoPoint(14.513795, 120.611603),
            GeoPoint(14.513783, 120.611552),
            GeoPoint(14.513530, 120.611614),
            GeoPoint(14.513503, 120.611608),
            GeoPoint(14.513493, 120.611584),
            GeoPoint(14.513492, 120.611551),
            GeoPoint(14.513287, 120.610783),
            GeoPoint(14.513269, 120.610662),
            GeoPoint(14.513181, 120.610194),
            GeoPoint(14.513159, 120.610151),
            GeoPoint(14.513128, 120.610130),
            GeoPoint(14.513089, 120.610118),
            GeoPoint(14.512712, 120.610146),
            GeoPoint(14.512499, 120.610191),
            GeoPoint(14.512299, 120.610257),
            GeoPoint(14.512245, 120.610305),
            GeoPoint(14.511744, 120.610836),
            GeoPoint(14.511439, 120.611053),
            GeoPoint(14.511386, 120.611061),
            GeoPoint(14.510444, 120.611090),
            GeoPoint(14.510399, 120.611102),
            GeoPoint(14.498413, 120.581816),
            GeoPoint(14.492929, 120.544899),
            GeoPoint(14.546852, 120.478512)
        ),
        listOf(/* Points for polygonMariveles */
            GeoPoint(14.469206, 120.393624),
            GeoPoint(14.472722, 120.405861),
            GeoPoint(14.483021, 120.424135),
            GeoPoint(14.489308, 120.437760),
            GeoPoint(14.520076, 120.468303),
            GeoPoint(14.546048, 120.478512),
            GeoPoint(14.546852, 120.478512),
            GeoPoint(14.492929, 120.544899),
            GeoPoint(14.498413, 120.581816),
            GeoPoint(14.510399, 120.611102),
            GeoPoint(14.507784, 120.610976),
            GeoPoint(14.485666, 120.609805),
            GeoPoint(14.479216, 120.609034),
            GeoPoint(14.468896, 120.606930),
            GeoPoint(14.461767, 120.602092),
            GeoPoint(14.450292, 120.592836),
            GeoPoint(14.444928, 120.587857),
            GeoPoint(14.441057, 120.585193),
            GeoPoint(14.431686, 120.576428),
            GeoPoint(14.425371, 120.571169),
            GeoPoint(14.423265, 120.562825),
            GeoPoint(14.420549, 120.539685),
            GeoPoint(14.422450, 120.520051),
            GeoPoint(14.407645, 120.499927),
            GeoPoint(14.407306, 120.497893),
            GeoPoint(14.409140, 120.477138),
            GeoPoint(14.414403, 120.469460),
            GeoPoint(14.419530, 120.463394),
            GeoPoint(14.427272, 120.458048),
            GeoPoint(14.435165, 120.454598),
            GeoPoint(14.440252, 120.448177),
            GeoPoint(14.441060, 120.439380),
            GeoPoint(14.440939, 120.423536),
            GeoPoint(14.445461, 120.420367),
            GeoPoint(14.451638, 120.414572),
            GeoPoint(14.453940, 120.399854),
            GeoPoint(14.457412, 120.392891),
            GeoPoint(14.458623, 120.389264),
            GeoPoint(14.460319, 120.388347),
            GeoPoint(14.462216, 120.388305),
            GeoPoint(14.463952, 120.389347),
            GeoPoint(14.465002, 120.391625),
            GeoPoint(14.466415, 120.392313),
            GeoPoint(14.468151, 120.393814)
        ),
        listOf(/* Points for polygonMorong */
            GeoPoint(14.806809, 120.349838),
            GeoPoint(14.797890, 120.345104),
            GeoPoint(14.769487, 120.348746),
            GeoPoint(14.762445, 120.346561),
            GeoPoint(14.755285, 120.348382),
            GeoPoint(14.743781, 120.315608),
            GeoPoint(14.739218, 120.311748),
            GeoPoint(14.735150, 120.303251),
            GeoPoint(14.733822, 120.294410),
            GeoPoint(14.735150, 120.283681),
            GeoPoint(14.734403, 120.279733),
            GeoPoint(14.731996, 120.276386),
            GeoPoint(14.731830, 120.273983),
            GeoPoint(14.733739, 120.273124),
            GeoPoint(14.735233, 120.257246),
            GeoPoint(14.734793, 120.255783),
            GeoPoint(14.734676, 120.254080),
            GeoPoint(14.727750, 120.251516),
            GeoPoint(14.727060, 120.251015),
            GeoPoint(14.726517, 120.250544),
            GeoPoint(14.725959, 120.249862),
            GeoPoint(14.725681, 120.249998),
            GeoPoint(14.725813, 120.250620),
            GeoPoint(14.725094, 120.251500),
            GeoPoint(14.724949, 120.251787),
            GeoPoint(14.724648, 120.252136),
            GeoPoint(14.724307, 120.252277),
            GeoPoint(14.724226, 120.252751),
            GeoPoint(14.724729, 120.253073),
            GeoPoint(14.724835, 120.253236),
            GeoPoint(14.724813, 120.253475),
            GeoPoint(14.723955, 120.255232),
            GeoPoint(14.723705, 120.255478),
            GeoPoint(14.723467, 120.255641),
            GeoPoint(14.723262, 120.255740),
            GeoPoint(14.723001, 120.255778),
            GeoPoint(14.722388, 120.255804),
            GeoPoint(14.722047, 120.255785),
            GeoPoint(14.721486, 120.255543),
            GeoPoint(14.720558, 120.254959),
            GeoPoint(14.719259, 120.253631),
            GeoPoint(14.719154, 120.253421),
            GeoPoint(14.719130, 120.253247),
            GeoPoint(14.719110, 120.253123),
            GeoPoint(14.719121, 120.252987),
            GeoPoint(14.719191, 120.252684),
            GeoPoint(14.719201, 120.252537),
            GeoPoint(14.719172, 120.252373),
            GeoPoint(14.719134, 120.252265),
            GeoPoint(14.719029, 120.252075),
            GeoPoint(14.718633, 120.251575),
            GeoPoint(14.716521, 120.250433),
            GeoPoint(14.716320, 120.250304),
            GeoPoint(14.716199, 120.250178),
            GeoPoint(14.716040, 120.249842),
            GeoPoint(14.715957, 120.249746),
            GeoPoint(14.715877, 120.249671),
            GeoPoint(14.715778, 120.249617),
            GeoPoint(14.715223, 120.249454),
            GeoPoint(14.715010, 120.249435),
            GeoPoint(14.714216, 120.249802),
            GeoPoint(14.713983, 120.250021),
            GeoPoint(14.713848, 120.250195),
            GeoPoint(14.713815, 120.250280),
            GeoPoint(14.713774, 120.250393),
            GeoPoint(14.713431, 120.250849),
            GeoPoint(14.712840, 120.251492),
            GeoPoint(14.712489, 120.251728),
            GeoPoint(14.712223, 120.251861),
            GeoPoint(14.711903, 120.251986),
            GeoPoint(14.711554, 120.252070),
            GeoPoint(14.711359, 120.252091),
            GeoPoint(14.711275, 120.252093),
            GeoPoint(14.711087, 120.252054),
            GeoPoint(14.710978, 120.252025),
            GeoPoint(14.710593, 120.251848),
            GeoPoint(14.709981, 120.251346),
            GeoPoint(14.709936, 120.251320),
            GeoPoint(14.709881, 120.251306),
            GeoPoint(14.709776, 120.251284),
            GeoPoint(14.709654, 120.251269),
            GeoPoint(14.709317, 120.251183),
            GeoPoint(14.709058, 120.251059),
            GeoPoint(14.709005, 120.251009),
            GeoPoint(14.708958, 120.250940),
            GeoPoint(14.708810, 120.250664),
            GeoPoint(14.708681, 120.250577),
            GeoPoint(14.708519, 120.250512),
            GeoPoint(14.708449, 120.250462),
            GeoPoint(14.708314, 120.250390),
            GeoPoint(14.708140, 120.250323),
            GeoPoint(14.707976, 120.250246),
            GeoPoint(14.707902, 120.250223),
            GeoPoint(14.707586, 120.250059),
            GeoPoint(14.707268, 120.249907),
            GeoPoint(14.707136, 120.249835),
            GeoPoint(14.706942, 120.249706),
            GeoPoint(14.706781, 120.249611),
            GeoPoint(14.706677, 120.249553),
            GeoPoint(14.705025, 120.249672),
            GeoPoint(14.704879, 120.249654),
            GeoPoint(14.703910, 120.249332),
            GeoPoint(14.702713, 120.248865),
            GeoPoint(14.702337, 120.248741),
            GeoPoint(14.702134, 120.248705),
            GeoPoint(14.701447, 120.248734),
            GeoPoint(14.701151, 120.248738),
            GeoPoint(14.700740, 120.248705),
            GeoPoint(14.699994, 120.248641),
            GeoPoint(14.699813, 120.248626),
            GeoPoint(14.699608, 120.248637),
            GeoPoint(14.699431, 120.248757),
            GeoPoint(14.699309, 120.248881),
            GeoPoint(14.698914, 120.249370),
            GeoPoint(14.698055, 120.249759),
            GeoPoint(14.697854, 120.249953),
            GeoPoint(14.697715, 120.250021),
            GeoPoint(14.694241, 120.248618),
            GeoPoint(14.693966, 120.248523),
            GeoPoint(14.692348, 120.248253),
            GeoPoint(14.692125, 120.248293),
            GeoPoint(14.691903, 120.248352),
            GeoPoint(14.689908, 120.250025),
            GeoPoint(14.687921, 120.251190),
            GeoPoint(14.685922, 120.251997),
            GeoPoint(14.684521, 120.252850),
            GeoPoint(14.683038, 120.254548),
            GeoPoint(14.682652, 120.255793),
            GeoPoint(14.682532, 120.256616),
            GeoPoint(14.681992, 120.257685),
            GeoPoint(14.680406, 120.259430),
            GeoPoint(14.677355, 120.262222),
            GeoPoint(14.677261, 120.262236),
            GeoPoint(14.676345, 120.263023),
            GeoPoint(14.674621, 120.264167),
            GeoPoint(14.674442, 120.264247),
            GeoPoint(14.673967, 120.264402),
            GeoPoint(14.673774, 120.264504),
            GeoPoint(14.673630, 120.264635),
            GeoPoint(14.673492, 120.264776),
            GeoPoint(14.672450, 120.265190),
            GeoPoint(14.671718, 120.265201),
            GeoPoint(14.671437, 120.265126),
            GeoPoint(14.671050, 120.264942),
            GeoPoint(14.670821, 120.264894),
            GeoPoint(14.670559, 120.264907),
            GeoPoint(14.670348, 120.264950),
            GeoPoint(14.669681, 120.265164),
            GeoPoint(14.669055, 120.265499),
            GeoPoint(14.668888, 120.265647),
            GeoPoint(14.668153, 120.266835),
            GeoPoint(14.668064, 120.267041),
            GeoPoint(14.668041, 120.267244),
            GeoPoint(14.668042, 120.267441),
            GeoPoint(14.668088, 120.267647),
            GeoPoint(14.668206, 120.267929),
            GeoPoint(14.668523, 120.268427),
            GeoPoint(14.668626, 120.268628),
            GeoPoint(14.668706, 120.268952),
            GeoPoint(14.669232, 120.270056),
            GeoPoint(14.669363, 120.270224),
            GeoPoint(14.669711, 120.270509),
            GeoPoint(14.670221, 120.271740),
            GeoPoint(14.670369, 120.272607),
            GeoPoint(14.670400, 120.273036),
            GeoPoint(14.670315, 120.273985),
            GeoPoint(14.669980, 120.275278),
            GeoPoint(14.669142, 120.276943),
            GeoPoint(14.666313, 120.280773),
            GeoPoint(14.666197, 120.280963),
            GeoPoint(14.666114, 120.281269),
            GeoPoint(14.666024, 120.281494),
            GeoPoint(14.665822, 120.281770),
            GeoPoint(14.664591, 120.283021),
            GeoPoint(14.663992, 120.283488),
            GeoPoint(14.662165, 120.285871),
            GeoPoint(14.661878, 120.286131),
            GeoPoint(14.660559, 120.287695),
            GeoPoint(14.660353, 120.287875),
            GeoPoint(14.659947, 120.288183),
            GeoPoint(14.659112, 120.289153),
            GeoPoint(14.658996, 120.289316),
            GeoPoint(14.658830, 120.289438),
            GeoPoint(14.658572, 120.289526),
            GeoPoint(14.658215, 120.289802),
            GeoPoint(14.657910, 120.290186),
            GeoPoint(14.656757, 120.291118),
            GeoPoint(14.655055, 120.292715),
            GeoPoint(14.653447, 120.293875),
            GeoPoint(14.653277, 120.293963),
            GeoPoint(14.652902, 120.294071),
            GeoPoint(14.652670, 120.294163),
            GeoPoint(14.648937, 120.296253),
            GeoPoint(14.648491, 120.296554),
            GeoPoint(14.645208, 120.299887),
            GeoPoint(14.643894, 120.300780),
            GeoPoint(14.640464, 120.302518),
            GeoPoint(14.639537, 120.302731),
            GeoPoint(14.638673, 120.302741),
            GeoPoint(14.638424, 120.302671),
            GeoPoint(14.638217, 120.302453),
            GeoPoint(14.638096, 120.302232),
            GeoPoint(14.637910, 120.302037),
            GeoPoint(14.637643, 120.301941),
            GeoPoint(14.635931, 120.302037),
            GeoPoint(14.635593, 120.302140),
            GeoPoint(14.635318, 120.302291),
            GeoPoint(14.635158, 120.302571),
            GeoPoint(14.635143, 120.302711),
            GeoPoint(14.635321, 120.302815),
            GeoPoint(14.635903, 120.302748),
            GeoPoint(14.636184, 120.302807),
            GeoPoint(14.636388, 120.302969),
            GeoPoint(14.636548, 120.303047),
            GeoPoint(14.636659, 120.303179),
            GeoPoint(14.636840, 120.303695),
            GeoPoint(14.636912, 120.304119),
            GeoPoint(14.636848, 120.304735),
            GeoPoint(14.636933, 120.305022),
            GeoPoint(14.637632, 120.305604),
            GeoPoint(14.637803, 120.305914),
            GeoPoint(14.637899, 120.306304),
            GeoPoint(14.638620, 120.307716),
            GeoPoint(14.639094, 120.308357),
            GeoPoint(14.639154, 120.308519),
            GeoPoint(14.639126, 120.308777),
            GeoPoint(14.639005, 120.308936),
            GeoPoint(14.638926, 120.309113),
            GeoPoint(14.638969, 120.309293),
            GeoPoint(14.639094, 120.309441),
            GeoPoint(14.639465, 120.309577),
            GeoPoint(14.639668, 120.309739),
            GeoPoint(14.639932, 120.310214),
            GeoPoint(14.640217, 120.311080),
            GeoPoint(14.640413, 120.311353),
            GeoPoint(14.640467, 120.311560),
            GeoPoint(14.640470, 120.311736),
            GeoPoint(14.640402, 120.311980),
            GeoPoint(14.640039, 120.312614),
            GeoPoint(14.639850, 120.312724),
            GeoPoint(14.639600, 120.312790),
            GeoPoint(14.639393, 120.312820),
            GeoPoint(14.639197, 120.312761),
            GeoPoint(14.638659, 120.312580),
            GeoPoint(14.637027, 120.312879),
            GeoPoint(14.635924, 120.312704),
            GeoPoint(14.635674, 120.312695),
            GeoPoint(14.635309, 120.312787),
            GeoPoint(14.634381, 120.312835),
            GeoPoint(14.634080, 120.312783),
            GeoPoint(14.632239, 120.311740),
            GeoPoint(14.630596, 120.311303),
            GeoPoint(14.630316, 120.311272),
            GeoPoint(14.629913, 120.311114),
            GeoPoint(14.629774, 120.310983),
            GeoPoint(14.629638, 120.310956),
            GeoPoint(14.629489, 120.311005),
            GeoPoint(14.629311, 120.311154),
            GeoPoint(14.628790, 120.311228),
            GeoPoint(14.628247, 120.311425),
            GeoPoint(14.627717, 120.311439),
            GeoPoint(14.627225, 120.311355),
            GeoPoint(14.626971, 120.311469),
            GeoPoint(14.626750, 120.311640),
            GeoPoint(14.626169, 120.312249),
            GeoPoint(14.625957, 120.313007),
            GeoPoint(14.625944, 120.314011),
            GeoPoint(14.625932, 120.314340),
            GeoPoint(14.626025, 120.314686),
            GeoPoint(14.626203, 120.314940),
            GeoPoint(14.626517, 120.315168),
            GeoPoint(14.627361, 120.315549),
            GeoPoint(14.627522, 120.315681),
            GeoPoint(14.629328, 120.317570),
            GeoPoint(14.629528, 120.317815),
            GeoPoint(14.629642, 120.318078),
            GeoPoint(14.630567, 120.319222),
            GeoPoint(14.630639, 120.319366),
            GeoPoint(14.630651, 120.319691),
            GeoPoint(14.630766, 120.319835),
            GeoPoint(14.631296, 120.321396),
            GeoPoint(14.631444, 120.321745),
            GeoPoint(14.631389, 120.321919),
            GeoPoint(14.631344, 120.322169),
            GeoPoint(14.631113, 120.322689),
            GeoPoint(14.631014, 120.323099),
            GeoPoint(14.631047, 120.323611),
            GeoPoint(14.631135, 120.323842),
            GeoPoint(14.631734, 120.324540),
            GeoPoint(14.632446, 120.324533),
            GeoPoint(14.632526, 120.324563),
            GeoPoint(14.632809, 120.324988),
            GeoPoint(14.636197, 120.328755),
            GeoPoint(14.636145, 120.332258),
            GeoPoint(14.635968, 120.332762),
            GeoPoint(14.635153, 120.333963),
            GeoPoint(14.634837, 120.334216),
            GeoPoint(14.633653, 120.334988),
            GeoPoint(14.633072, 120.335192),
            GeoPoint(14.632621, 120.335058),
            GeoPoint(14.631878, 120.334371),
            GeoPoint(14.631764, 120.334334),
            GeoPoint(14.631650, 120.334355),
            GeoPoint(14.631686, 120.334591),
            GeoPoint(14.632018, 120.335262),
            GeoPoint(14.631494, 120.337246),
            GeoPoint(14.631318, 120.337305),
            GeoPoint(14.631074, 120.337284),
            GeoPoint(14.630581, 120.337541),
            GeoPoint(14.630062, 120.337649),
            GeoPoint(14.629901, 120.337820),
            GeoPoint(14.630473, 120.341557),
            GeoPoint(14.630664, 120.341860),
            GeoPoint(14.628288, 120.345483),
            GeoPoint(14.626970, 120.347734),
            GeoPoint(14.626716, 120.347896),
            GeoPoint(14.626455, 120.348032),
            GeoPoint(14.625424, 120.347896),
            GeoPoint(14.624966, 120.347671),
            GeoPoint(14.624691, 120.347684),
            GeoPoint(14.624058, 120.347905),
            GeoPoint(14.621333, 120.347576),
            GeoPoint(14.620862, 120.347649),
            GeoPoint(14.619661, 120.348492),
            GeoPoint(14.619596, 120.348650),
            GeoPoint(14.619635, 120.348921),
            GeoPoint(14.620740, 120.351930),
            GeoPoint(14.620985, 120.352769),
            GeoPoint(14.624449, 120.356846),
            GeoPoint(14.676099, 120.425886),
            GeoPoint(14.694987, 120.435047),
            GeoPoint(14.700491, 120.427006),
            GeoPoint(14.716438, 120.433493),
            GeoPoint(14.727497, 120.438021),
            GeoPoint(14.728722, 120.437463),
            GeoPoint(14.735702, 120.416339),
            GeoPoint(14.744091, 120.394787),
            GeoPoint(14.844347, 120.358358)
        ),
        listOf(/* Points for polygonOrani */
            GeoPoint(14.788070, 120.546951),
            GeoPoint(14.787699, 120.545046),
            GeoPoint(14.786130, 120.543567),
            GeoPoint(14.786474, 120.541462),
            GeoPoint(14.786394, 120.540312),
            GeoPoint(14.786776, 120.531684),
            GeoPoint(14.788577, 120.524214),
            GeoPoint(14.789237, 120.522651),
            GeoPoint(14.788848, 120.520853),
            GeoPoint(14.789021, 120.518864),
            GeoPoint(14.788988, 120.517691),
            GeoPoint(14.788346, 120.516690),
            GeoPoint(14.789142, 120.514634),
            GeoPoint(14.788680, 120.512572),
            GeoPoint(14.789393, 120.509159),
            GeoPoint(14.789052, 120.507240),
            GeoPoint(14.788396, 120.505203),
            GeoPoint(14.786389, 120.502263),
            GeoPoint(14.785306, 120.498317),
            GeoPoint(14.785973, 120.495427),
            GeoPoint(14.786010, 120.493575),
            GeoPoint(14.786285, 120.491698),
            GeoPoint(14.786131, 120.490207),
            GeoPoint(14.785187, 120.488407),
            GeoPoint(14.784795, 120.486354),
            GeoPoint(14.784171, 120.485577),
            GeoPoint(14.784116, 120.484792),
            GeoPoint(14.781930, 120.483532),
            GeoPoint(14.781296, 120.480676),
            GeoPoint(14.781013, 120.480429),
            GeoPoint(14.776257, 120.478970),
            GeoPoint(14.775511, 120.477269),
            GeoPoint(14.774429, 120.476786),
            GeoPoint(14.774486, 120.475406),
            GeoPoint(14.770665, 120.472605),
            GeoPoint(14.770307, 120.470854),
            GeoPoint(14.769169, 120.469916),
            GeoPoint(14.768152, 120.468486),
            GeoPoint(14.767978, 120.466975),
            GeoPoint(14.767690, 120.465744),
            GeoPoint(14.767825, 120.463064),
            GeoPoint(14.767306, 120.461661),
            GeoPoint(14.767651, 120.460597),
            GeoPoint(14.766931, 120.459933),
            GeoPoint(14.765147, 120.459248),
            GeoPoint(14.764680, 120.457218),
            GeoPoint(14.763908, 120.455977),
            GeoPoint(14.763965, 120.454773),
            GeoPoint(14.762307, 120.454355),
            GeoPoint(14.760654, 120.452154),
            GeoPoint(14.757819, 120.450147),
            GeoPoint(14.758019, 120.448469),
            GeoPoint(14.752752, 120.443298),
            GeoPoint(14.752647, 120.441056),
            GeoPoint(14.751389, 120.439883),
            GeoPoint(14.751537, 120.437312),
            GeoPoint(14.750028, 120.436545),
            GeoPoint(14.748496, 120.433839),
            GeoPoint(14.749800, 120.430392),
            GeoPoint(14.738190, 120.422353),
            GeoPoint(14.735702, 120.416339),
            GeoPoint(14.744091, 120.394787),
            GeoPoint(14.758469, 120.414382),
            GeoPoint(14.762579, 120.424005),
            GeoPoint(14.765940, 120.427438),
            GeoPoint(14.767725, 120.431429),
            GeoPoint(14.787994, 120.443625),
            GeoPoint(14.794177, 120.446071),
            GeoPoint(14.797745, 120.448990),
            GeoPoint(14.799239, 120.450620),
            GeoPoint(14.799986, 120.452294),
            GeoPoint(14.803181, 120.453539),
            GeoPoint(14.806209, 120.456543),
            GeoPoint(14.817038, 120.460534),
            GeoPoint(14.818407, 120.461349),
            GeoPoint(14.818822, 120.463538),
            GeoPoint(14.818075, 120.466199),
            GeoPoint(14.807579, 120.478987),
            GeoPoint(14.807205, 120.481219),
            GeoPoint(14.807330, 120.484094),
            GeoPoint(14.807993, 120.485768),
            GeoPoint(14.810441, 120.490617),
            GeoPoint(14.813138, 120.500102),
            GeoPoint(14.820357, 120.515680),
            GeoPoint(14.822307, 120.518126),
            GeoPoint(14.823012, 120.519671),
            GeoPoint(14.822888, 120.521688),
            GeoPoint(14.814302, 120.540840),
            GeoPoint(14.813875, 120.540969),
            GeoPoint(14.813443, 120.540701),
            GeoPoint(14.812888, 120.540038),
            GeoPoint(14.811556, 120.539566),
            GeoPoint(14.811366, 120.539449),
            GeoPoint(14.809417, 120.538792),
            GeoPoint(14.809214, 120.539028),
            GeoPoint(14.808357, 120.538849),
            GeoPoint(14.807832, 120.538817),
            GeoPoint(14.807037, 120.538894),
            GeoPoint(14.806741, 120.538881),
            GeoPoint(14.806636, 120.538734),
            GeoPoint(14.806519, 120.538805),
            GeoPoint(14.806430, 120.539034),
            GeoPoint(14.806476, 120.539557),
            GeoPoint(14.806436, 120.539717),
            GeoPoint(14.806402, 120.539947),
            GeoPoint(14.806304, 120.539966),
            GeoPoint(14.806211, 120.539927),
            GeoPoint(14.805989, 120.539905),
            GeoPoint(14.805940, 120.540016),
            GeoPoint(14.805977, 120.540108),
            GeoPoint(14.806045, 120.540147),
            GeoPoint(14.806060, 120.540246),
            GeoPoint(14.806049, 120.540311),
            GeoPoint(14.806108, 120.540375),
            GeoPoint(14.806089, 120.540424),
            GeoPoint(14.805947, 120.540372),
            GeoPoint(14.805886, 120.540366),
            GeoPoint(14.805807, 120.540388),
            GeoPoint(14.805482, 120.540194),
            GeoPoint(14.804909, 120.540377),
            GeoPoint(14.804774, 120.540675),
            GeoPoint(14.803938, 120.541949),
            GeoPoint(14.803869, 120.542001),
            GeoPoint(14.803819, 120.542080),
            GeoPoint(14.803666, 120.542238),
            GeoPoint(14.803611, 120.542303),
            GeoPoint(14.803459, 120.542335),
            GeoPoint(14.803387, 120.542330),
            GeoPoint(14.802733, 120.542958),
            GeoPoint(14.802479, 120.543146),
            GeoPoint(14.802149, 120.543327),
            GeoPoint(14.801650, 120.543722),
            GeoPoint(14.801630, 120.543783),
            GeoPoint(14.801452, 120.543810),
            GeoPoint(14.801347, 120.543778),
            GeoPoint(14.801229, 120.543762),
            GeoPoint(14.801037, 120.543864),
            GeoPoint(14.800848, 120.544116),
            GeoPoint(14.800769, 120.544218),
            GeoPoint(14.800669, 120.544301),
            GeoPoint(14.800281, 120.544491),
            GeoPoint(14.800006, 120.544673),
            GeoPoint(14.799768, 120.544752),
            GeoPoint(14.799681, 120.544788),
            GeoPoint(14.799297, 120.544743),
            GeoPoint(14.796364, 120.546738),
            GeoPoint(14.795407, 120.547212),
            GeoPoint(14.795257, 120.547241),
            GeoPoint(14.793439, 120.547816),
            GeoPoint(14.793057, 120.548074),
            GeoPoint(14.792683, 120.548252),
            GeoPoint(14.792078, 120.548627),
            GeoPoint(14.791994, 120.548629),
            GeoPoint(14.791937, 120.548603),
            GeoPoint(14.791877, 120.548548),
            GeoPoint(14.791783, 120.548521),
            GeoPoint(14.791546, 120.548443),
            GeoPoint(14.791501, 120.548451),
            GeoPoint(14.791194, 120.548574),
            GeoPoint(14.790984, 120.548715),
            GeoPoint(14.790708, 120.548787),
            GeoPoint(14.790379, 120.548754),
            GeoPoint(14.790312, 120.548724),
            GeoPoint(14.790227, 120.548656),
            GeoPoint(14.788643, 120.546909)
        ),
        listOf(/* Points for polygonOrion */
            GeoPoint(14.662856, 120.574886),
            GeoPoint(14.647783, 120.564737),
            GeoPoint(14.643049, 120.564750),
            GeoPoint(14.639810, 120.562835),
            GeoPoint(14.635983, 120.557794),
            GeoPoint(14.631750, 120.530500),
            GeoPoint(14.624937, 120.525521),
            GeoPoint(14.610985, 120.531947),
            GeoPoint(14.596199, 120.525854),
            GeoPoint(14.588905, 120.515389),
            GeoPoint(14.589221, 120.501306),
            GeoPoint(14.591886, 120.486449),
            GeoPoint(14.598864, 120.479174),
            GeoPoint(14.549960, 120.478069),
            GeoPoint(14.556409, 120.498225),
            GeoPoint(14.568461, 120.537573),
            GeoPoint(14.569202, 120.548648),
            GeoPoint(14.569449, 120.573145),
            GeoPoint(14.570067, 120.577917),
            GeoPoint(14.571277, 120.583556),
            GeoPoint(14.571622, 120.589042),
            GeoPoint(14.571647, 120.590318),
            GeoPoint(14.572956, 120.594299),
            GeoPoint(14.575303, 120.593882),
            GeoPoint(14.575930, 120.593616),
            GeoPoint(14.576261, 120.593605),
            GeoPoint(14.576620, 120.593688),
            GeoPoint(14.576936, 120.593681),
            GeoPoint(14.577057, 120.593624),
            GeoPoint(14.577186, 120.593487),
            GeoPoint(14.577233, 120.593415),
            GeoPoint(14.577554, 120.592843),
            GeoPoint(14.577741, 120.592650),
            GeoPoint(14.577959, 120.592532),
            GeoPoint(14.578392, 120.592398),
            GeoPoint(14.578941, 120.592328),
            GeoPoint(14.579208, 120.592339),
            GeoPoint(14.579481, 120.592380),
            GeoPoint(14.579601, 120.592403),
            GeoPoint(14.579732, 120.592457),
            GeoPoint(14.579902, 120.592638),
            GeoPoint(14.580006, 120.592854),
            GeoPoint(14.580042, 120.592981),
            GeoPoint(14.580078, 120.593038),
            GeoPoint(14.580123, 120.593084),
            GeoPoint(14.580283, 120.593183),
            GeoPoint(14.580403, 120.593213),
            GeoPoint(14.580597, 120.593229),
            GeoPoint(14.580664, 120.593216),
            GeoPoint(14.580726, 120.593193),
            GeoPoint(14.580791, 120.593146),
            GeoPoint(14.581012, 120.592962),
            GeoPoint(14.581111, 120.592857),
            GeoPoint(14.581173, 120.592772),
            GeoPoint(14.581332, 120.592483),
            GeoPoint(14.581383, 120.592302),
            GeoPoint(14.581383, 120.592154),
            GeoPoint(14.581397, 120.592035),
            GeoPoint(14.581432, 120.591924),
            GeoPoint(14.581462, 120.591892),
            GeoPoint(14.581743, 120.591731),
            GeoPoint(14.582011, 120.591641),
            GeoPoint(14.582244, 120.591605),
            GeoPoint(14.582434, 120.591601),
            GeoPoint(14.582763, 120.591684),
            GeoPoint(14.583059, 120.591824),
            GeoPoint(14.583148, 120.591910),
            GeoPoint(14.583188, 120.591944),
            GeoPoint(14.583235, 120.591955),
            GeoPoint(14.583298, 120.591955),
            GeoPoint(14.583481, 120.591911),
            GeoPoint(14.583843, 120.591746),
            GeoPoint(14.583932, 120.591718),
            GeoPoint(14.584246, 120.591659),
            GeoPoint(14.584313, 120.591612),
            GeoPoint(14.584332, 120.591569),
            GeoPoint(14.584372, 120.591353),
            GeoPoint(14.584389, 120.591291),
            GeoPoint(14.584417, 120.591227),
            GeoPoint(14.584448, 120.591186),
            GeoPoint(14.584760, 120.590956),
            GeoPoint(14.586276, 120.592804),
            GeoPoint(14.586340, 120.592822),
            GeoPoint(14.587060, 120.592252),
            GeoPoint(14.587090, 120.592182),
            GeoPoint(14.587097, 120.592116),
            GeoPoint(14.586043, 120.590727),
            GeoPoint(14.585997, 120.590544),
            GeoPoint(14.586006, 120.589662),
            GeoPoint(14.586119, 120.589378),
            GeoPoint(14.587108, 120.588361),
            GeoPoint(14.587912, 120.588126),
            GeoPoint(14.588093, 120.588185),
            GeoPoint(14.588148, 120.588229),
            GeoPoint(14.588186, 120.588287),
            GeoPoint(14.588207, 120.588347),
            GeoPoint(14.588247, 120.588416),
            GeoPoint(14.588412, 120.588560),
            GeoPoint(14.588444, 120.588624),
            GeoPoint(14.588444, 120.588662),
            GeoPoint(14.588433, 120.588693),
            GeoPoint(14.588406, 120.588715),
            GeoPoint(14.588360, 120.588793),
            GeoPoint(14.588354, 120.588852),
            GeoPoint(14.588368, 120.588940),
            GeoPoint(14.588536, 120.589363),
            GeoPoint(14.588570, 120.589397),
            GeoPoint(14.588605, 120.589414),
            GeoPoint(14.588664, 120.589404),
            GeoPoint(14.588682, 120.589386),
            GeoPoint(14.588704, 120.589337),
            GeoPoint(14.588717, 120.589248),
            GeoPoint(14.588717, 120.589184),
            GeoPoint(14.588697, 120.589047),
            GeoPoint(14.588576, 120.588598),
            GeoPoint(14.588516, 120.588409),
            GeoPoint(14.588521, 120.588360),
            GeoPoint(14.588680, 120.588067),
            GeoPoint(14.588764, 120.587886),
            GeoPoint(14.588828, 120.587699),
            GeoPoint(14.588878, 120.587637),
            GeoPoint(14.588947, 120.587581),
            GeoPoint(14.589037, 120.587532),
            GeoPoint(14.589141, 120.587490),
            GeoPoint(14.589757, 120.587333),
            GeoPoint(14.590566, 120.587232),
            GeoPoint(14.590638, 120.587243),
            GeoPoint(14.590727, 120.587285),
            GeoPoint(14.590799, 120.587351),
            GeoPoint(14.590849, 120.587447),
            GeoPoint(14.591115, 120.588341),
            GeoPoint(14.591142, 120.588460),
            GeoPoint(14.591169, 120.588489),
            GeoPoint(14.591204, 120.588499),
            GeoPoint(14.592490, 120.588177),
            GeoPoint(14.592549, 120.588149),
            GeoPoint(14.592587, 120.588123),
            GeoPoint(14.592608, 120.588096),
            GeoPoint(14.592609, 120.588045),
            GeoPoint(14.592290, 120.586534),
            GeoPoint(14.592305, 120.586507),
            GeoPoint(14.592332, 120.586494),
            GeoPoint(14.592399, 120.586508),
            GeoPoint(14.592555, 120.586653),
            GeoPoint(14.592700, 120.586709),
            GeoPoint(14.592861, 120.586739),
            GeoPoint(14.593061, 120.586754),
            GeoPoint(14.593362, 120.586761),
            GeoPoint(14.593700, 120.586759),
            GeoPoint(14.594500, 120.586582),
            GeoPoint(14.594834, 120.586474),
            GeoPoint(14.595027, 120.586439),
            GeoPoint(14.595116, 120.586435),
            GeoPoint(14.595206, 120.586443),
            GeoPoint(14.595268, 120.586464),
            GeoPoint(14.595353, 120.586543),
            GeoPoint(14.595471, 120.586601),
            GeoPoint(14.595601, 120.586627),
            GeoPoint(14.595682, 120.586629),
            GeoPoint(14.595754, 120.586621),
            GeoPoint(14.595810, 120.586607),
            GeoPoint(14.595867, 120.586604),
            GeoPoint(14.595897, 120.586618),
            GeoPoint(14.595927, 120.586643),
            GeoPoint(14.595953, 120.586714),
            GeoPoint(14.595983, 120.586838),
            GeoPoint(14.596036, 120.586892),
            GeoPoint(14.596174, 120.586930),
            GeoPoint(14.596364, 120.586930),
            GeoPoint(14.596549, 120.586905),
            GeoPoint(14.596706, 120.586889),
            GeoPoint(14.596999, 120.586890),
            GeoPoint(14.597186, 120.586905),
            GeoPoint(14.597379, 120.586932),
            GeoPoint(14.597576, 120.586953),
            GeoPoint(14.597716, 120.586958),
            GeoPoint(14.597907, 120.586932),
            GeoPoint(14.598095, 120.586900),
            GeoPoint(14.598242, 120.586858),
            GeoPoint(14.598371, 120.586790),
            GeoPoint(14.598555, 120.586677),
            GeoPoint(14.598619, 120.586653),
            GeoPoint(14.598680, 120.586640),
            GeoPoint(14.598722, 120.586646),
            GeoPoint(14.598791, 120.586687),
            GeoPoint(14.598878, 120.586718),
            GeoPoint(14.598943, 120.586715),
            GeoPoint(14.599005, 120.586701),
            GeoPoint(14.599068, 120.586681),
            GeoPoint(14.599122, 120.586650),
            GeoPoint(14.599240, 120.586546),
            GeoPoint(14.599405, 120.586434),
            GeoPoint(14.599591, 120.586361),
            GeoPoint(14.599797, 120.586292),
            GeoPoint(14.600046, 120.586239),
            GeoPoint(14.600258, 120.586219),
            GeoPoint(14.601109, 120.586146),
            GeoPoint(14.601358, 120.586132),
            GeoPoint(14.601669, 120.586116),
            GeoPoint(14.602006, 120.586083),
            GeoPoint(14.602112, 120.586053),
            GeoPoint(14.602249, 120.585988),
            GeoPoint(14.602321, 120.585966),
            GeoPoint(14.602395, 120.585964),
            GeoPoint(14.602615, 120.586007),
            GeoPoint(14.602762, 120.586073),
            GeoPoint(14.602942, 120.586110),
            GeoPoint(14.603799, 120.586098),
            GeoPoint(14.604352, 120.586122),
            GeoPoint(14.605065, 120.586073),
            GeoPoint(14.606276, 120.585928),
            GeoPoint(14.606589, 120.585870),
            GeoPoint(14.606974, 120.585720),
            GeoPoint(14.607084, 120.585650),
            GeoPoint(14.607159, 120.585555),
            GeoPoint(14.607199, 120.585476),
            GeoPoint(14.607288, 120.585197),
            GeoPoint(14.607305, 120.585110),
            GeoPoint(14.607304, 120.584950),
            GeoPoint(14.607328, 120.584915),
            GeoPoint(14.607392, 120.584926),
            GeoPoint(14.607545, 120.585140),
            GeoPoint(14.607584, 120.585221),
            GeoPoint(14.607765, 120.585759),
            GeoPoint(14.607873, 120.585875),
            GeoPoint(14.607970, 120.585895),
            GeoPoint(14.610221, 120.585902),
            GeoPoint(14.611575, 120.585944),
            GeoPoint(14.611705, 120.585978),
            GeoPoint(14.612172, 120.585903),
            GeoPoint(14.612834, 120.585993),
            GeoPoint(14.613345, 120.585977),
            GeoPoint(14.613733, 120.585890),
            GeoPoint(14.613812, 120.585834),
            GeoPoint(14.613969, 120.585575),
            GeoPoint(14.614039, 120.585517),
            GeoPoint(14.614767, 120.585106),
            GeoPoint(14.614815, 120.584915),
            GeoPoint(14.614867, 120.584799),
            GeoPoint(14.614877, 120.584705),
            GeoPoint(14.614892, 120.584219),
            GeoPoint(14.614925, 120.584154),
            GeoPoint(14.614986, 120.584110),
            GeoPoint(14.615080, 120.584111),
            GeoPoint(14.615324, 120.584177),
            GeoPoint(14.615393, 120.584232),
            GeoPoint(14.615450, 120.584326),
            GeoPoint(14.615493, 120.584400),
            GeoPoint(14.615532, 120.584439),
            GeoPoint(14.615583, 120.584464),
            GeoPoint(14.615786, 120.584475),
            GeoPoint(14.616019, 120.584445),
            GeoPoint(14.616288, 120.584438),
            GeoPoint(14.616434, 120.584481),
            GeoPoint(14.616506, 120.584538),
            GeoPoint(14.616562, 120.584612),
            GeoPoint(14.616633, 120.585041),
            GeoPoint(14.616681, 120.585188),
            GeoPoint(14.616898, 120.585461),
            GeoPoint(14.617054, 120.585605),
            GeoPoint(14.617832, 120.585922),
            GeoPoint(14.618000, 120.585937),
            GeoPoint(14.618301, 120.585907),
            GeoPoint(14.618412, 120.585814),
            GeoPoint(14.620049, 120.585822),
            GeoPoint(14.622481, 120.585750),
            GeoPoint(14.622698, 120.585830),
            GeoPoint(14.622840, 120.586017),
            GeoPoint(14.622934, 120.586234),
            GeoPoint(14.622981, 120.586279),
            GeoPoint(14.623069, 120.586407),
            GeoPoint(14.623122, 120.586437),
            GeoPoint(14.623244, 120.586463),
            GeoPoint(14.623959, 120.586413),
            GeoPoint(14.624700, 120.586396),
            GeoPoint(14.624846, 120.586465),
            GeoPoint(14.624917, 120.586519),
            GeoPoint(14.625015, 120.586669),
            GeoPoint(14.625175, 120.586993),
            GeoPoint(14.625308, 120.587008),
            GeoPoint(14.625450, 120.586970),
            GeoPoint(14.625531, 120.586961),
            GeoPoint(14.625591, 120.586918),
            GeoPoint(14.625611, 120.586869),
            GeoPoint(14.625488, 120.586432),
            GeoPoint(14.625509, 120.586368),
            GeoPoint(14.625604, 120.586274),
            GeoPoint(14.626418, 120.585547),
            GeoPoint(14.626441, 120.585505),
            GeoPoint(14.626480, 120.585338),
            GeoPoint(14.626511, 120.585287),
            GeoPoint(14.626561, 120.585255),
            GeoPoint(14.626825, 120.585189),
            GeoPoint(14.627036, 120.585163),
            GeoPoint(14.627255, 120.585153),
            GeoPoint(14.627923, 120.585194),
            GeoPoint(14.628889, 120.585209),
            GeoPoint(14.628918, 120.585190),
            GeoPoint(14.628927, 120.585150),
            GeoPoint(14.628951, 120.584793),
            GeoPoint(14.628975, 120.584717),
            GeoPoint(14.629035, 120.584654),
            GeoPoint(14.629213, 120.584585),
            GeoPoint(14.629317, 120.584568),
            GeoPoint(14.629514, 120.584563),
            GeoPoint(14.630017, 120.584594),
            GeoPoint(14.630239, 120.584576),
            GeoPoint(14.630292, 120.584584),
            GeoPoint(14.630346, 120.584604),
            GeoPoint(14.630415, 120.584651),
            GeoPoint(14.630483, 120.584736),
            GeoPoint(14.630520, 120.584805),
            GeoPoint(14.630566, 120.584853),
            GeoPoint(14.630635, 120.584903),
            GeoPoint(14.630705, 120.584923),
            GeoPoint(14.631054, 120.584965),
            GeoPoint(14.632043, 120.584943),
            GeoPoint(14.632743, 120.584889),
            GeoPoint(14.633130, 120.584891),
            GeoPoint(14.633434, 120.584879),
            GeoPoint(14.634030, 120.584927),
            GeoPoint(14.634361, 120.584912),
            GeoPoint(14.634848, 120.584791),
            GeoPoint(14.635520, 120.584599),
            GeoPoint(14.636272, 120.584498),
            GeoPoint(14.637011, 120.584431),
            GeoPoint(14.637185, 120.584394),
            GeoPoint(14.637365, 120.584307),
            GeoPoint(14.637563, 120.584232),
            GeoPoint(14.637966, 120.584176),
            GeoPoint(14.638371, 120.584138),
            GeoPoint(14.639098, 120.584047),
            GeoPoint(14.639574, 120.583894),
            GeoPoint(14.640224, 120.583614),
            GeoPoint(14.640437, 120.583551),
            GeoPoint(14.640466, 120.583549),
            GeoPoint(14.640837, 120.583641),
            GeoPoint(14.640887, 120.583747),
            GeoPoint(14.640926, 120.584281),
            GeoPoint(14.640920, 120.584323),
            GeoPoint(14.640937, 120.584362),
            GeoPoint(14.640979, 120.584386),
            GeoPoint(14.641026, 120.584399),
            GeoPoint(14.641612, 120.584334),
            GeoPoint(14.641765, 120.584307),
            GeoPoint(14.641918, 120.584293),
            GeoPoint(14.642159, 120.584294),
            GeoPoint(14.642479, 120.584308),
            GeoPoint(14.642612, 120.584295),
            GeoPoint(14.642732, 120.584273),
            GeoPoint(14.642794, 120.584235),
            GeoPoint(14.642842, 120.584193),
            GeoPoint(14.642872, 120.584175),
            GeoPoint(14.643080, 120.584097),
            GeoPoint(14.643145, 120.584086),
            GeoPoint(14.643178, 120.584055),
            GeoPoint(14.643335, 120.583843),
            GeoPoint(14.643349, 120.583732),
            GeoPoint(14.643358, 120.583539),
            GeoPoint(14.643355, 120.583404),
            GeoPoint(14.643377, 120.583356),
            GeoPoint(14.643494, 120.583333),
            GeoPoint(14.644526, 120.583365),
            GeoPoint(14.645625, 120.583122),
            GeoPoint(14.645658, 120.583119),
            GeoPoint(14.645680, 120.583076),
            GeoPoint(14.645669, 120.582881),
            GeoPoint(14.645655, 120.582824),
            GeoPoint(14.645729, 120.582746),
            GeoPoint(14.652401, 120.580462),
            GeoPoint(14.653579, 120.579696),
            GeoPoint(14.654736, 120.579358),
            GeoPoint(14.655233, 120.578892),
            GeoPoint(14.656662, 120.578060),
            GeoPoint(14.656955, 120.577942),
            GeoPoint(14.658227, 120.577546),
            GeoPoint(14.659032, 120.577077),
            GeoPoint(14.659421, 120.576749),
            GeoPoint(14.660269, 120.576213)
        ),
        listOf(/* Points for polygonPilar */
            GeoPoint(14.631659, 120.476605),
            GeoPoint(14.633323, 120.476878),
            GeoPoint(14.634906, 120.478465),
            GeoPoint(14.636579, 120.479470),
            GeoPoint(14.637685, 120.481005),
            GeoPoint(14.638411, 120.482750),
            GeoPoint(14.638187, 120.484146),
            GeoPoint(14.637072, 120.485242),
            GeoPoint(14.637380, 120.485720),
            GeoPoint(14.637853, 120.486430),
            GeoPoint(14.638235, 120.487674),
            GeoPoint(14.639248, 120.488668),
            GeoPoint(14.639780, 120.489673),
            GeoPoint(14.640030, 120.491274),
            GeoPoint(14.639905, 120.493170),
            GeoPoint(14.640173, 120.496966),
            GeoPoint(14.639645, 120.498506),
            GeoPoint(14.637998, 120.500665),
            GeoPoint(14.637848, 120.502828),
            GeoPoint(14.637242, 120.504831),
            GeoPoint(14.635975, 120.506546),
            GeoPoint(14.634751, 120.505962),
            GeoPoint(14.634072, 120.506626),
            GeoPoint(14.634351, 120.507074),
            GeoPoint(14.635310, 120.514928),
            GeoPoint(14.635321, 120.517311),
            GeoPoint(14.635831, 120.518707),
            GeoPoint(14.635602, 120.521198),
            GeoPoint(14.636336, 120.523216),
            GeoPoint(14.637278, 120.524420),
            GeoPoint(14.638610, 120.526567),
            GeoPoint(14.639043, 120.530023),
            GeoPoint(14.639528, 120.530876),
            GeoPoint(14.641278, 120.531840),
            GeoPoint(14.643102, 120.532139),
            GeoPoint(14.643932, 120.534116),
            GeoPoint(14.643025, 120.535189),
            GeoPoint(14.644471, 120.536335),
            GeoPoint(14.648067, 120.536603),
            GeoPoint(14.648974, 120.536395),
            GeoPoint(14.650284, 120.534881),
            GeoPoint(14.651154, 120.534650),
            GeoPoint(14.651921, 120.535374),
            GeoPoint(14.652192, 120.536520),
            GeoPoint(14.653051, 120.537665),
            GeoPoint(14.654812, 120.537165),
            GeoPoint(14.656592, 120.537104),
            GeoPoint(14.658240, 120.536338),
            GeoPoint(14.660512, 120.537654),
            GeoPoint(14.661176, 120.539278),
            GeoPoint(14.660508, 120.540723),
            GeoPoint(14.661891, 120.543867),
            GeoPoint(14.662229, 120.546492),
            GeoPoint(14.662948, 120.547604),
            GeoPoint(14.665957, 120.549075),
            GeoPoint(14.667333, 120.549572),
            GeoPoint(14.668425, 120.549194),
            GeoPoint(14.670898, 120.547281),
            GeoPoint(14.672862, 120.546363),
            GeoPoint(14.675708, 120.543787),
            GeoPoint(14.676949, 120.544239),
            GeoPoint(14.677857, 120.545130),
            GeoPoint(14.680566, 120.548532),
            GeoPoint(14.681801, 120.549429),
            GeoPoint(14.685754, 120.550080),
            GeoPoint(14.686902, 120.557306),
            GeoPoint(14.689441, 120.559222),
            GeoPoint(14.690293, 120.560098),
            GeoPoint(14.691180, 120.561167),
            GeoPoint(14.692046, 120.562453),
            GeoPoint(14.692801, 120.563941),
            GeoPoint(14.694341, 120.566982),
            GeoPoint(14.689631, 120.566111),
            GeoPoint(14.688906, 120.566158),
            GeoPoint(14.688084, 120.566058),
            GeoPoint(14.687169, 120.565867),
            GeoPoint(14.685747, 120.566025),
            GeoPoint(14.685105, 120.566058),
            GeoPoint(14.684983, 120.566151),
            GeoPoint(14.684900, 120.566256),
            GeoPoint(14.684764, 120.566386),
            GeoPoint(14.684625, 120.566449),
            GeoPoint(14.684219, 120.566463),
            GeoPoint(14.682499, 120.566604),
            GeoPoint(14.681864, 120.566568),
            GeoPoint(14.680999, 120.566579),
            GeoPoint(14.680908, 120.566596),
            GeoPoint(14.680825, 120.566549),
            GeoPoint(14.680625, 120.566215),
            GeoPoint(14.680553, 120.566184),
            GeoPoint(14.680382, 120.566187),
            GeoPoint(14.680082, 120.566278),
            GeoPoint(14.679901, 120.566436),
            GeoPoint(14.679204, 120.566571),
            GeoPoint(14.678138, 120.566985),
            GeoPoint(14.678050, 120.567029),
            GeoPoint(14.677987, 120.567130),
            GeoPoint(14.677872, 120.567270),
            GeoPoint(14.677747, 120.567356),
            GeoPoint(14.677627, 120.567408),
            GeoPoint(14.677517, 120.567429),
            GeoPoint(14.677072, 120.567473),
            GeoPoint(14.676798, 120.567464),
            GeoPoint(14.676388, 120.567317),
            GeoPoint(14.676114, 120.567276),
            GeoPoint(14.676011, 120.567273),
            GeoPoint(14.675928, 120.567298),
            GeoPoint(14.675864, 120.567343),
            GeoPoint(14.675818, 120.567402),
            GeoPoint(14.675774, 120.567488),
            GeoPoint(14.675723, 120.567542),
            GeoPoint(14.675544, 120.567655),
            GeoPoint(14.674257, 120.568184),
            GeoPoint(14.673653, 120.568566),
            GeoPoint(14.673378, 120.568663),
            GeoPoint(14.673074, 120.568732),
            GeoPoint(14.672553, 120.568812),
            GeoPoint(14.672374, 120.568863),
            GeoPoint(14.672151, 120.568964),
            GeoPoint(14.670574, 120.569771),
            GeoPoint(14.670502, 120.569851),
            GeoPoint(14.670483, 120.569879),
            GeoPoint(14.670470, 120.570052),
            GeoPoint(14.670509, 120.570176),
            GeoPoint(14.670523, 120.570246),
            GeoPoint(14.670517, 120.570298),
            GeoPoint(14.670486, 120.570345),
            GeoPoint(14.670427, 120.570376),
            GeoPoint(14.670209, 120.570418),
            GeoPoint(14.670133, 120.570438),
            GeoPoint(14.669865, 120.570406),
            GeoPoint(14.669806, 120.570378),
            GeoPoint(14.669615, 120.570056),
            GeoPoint(14.669533, 120.569985),
            GeoPoint(14.669460, 120.569953),
            GeoPoint(14.669399, 120.569948),
            GeoPoint(14.668580, 120.570381),
            GeoPoint(14.668614, 120.570496),
            GeoPoint(14.668708, 120.570856),
            GeoPoint(14.668711, 120.570943),
            GeoPoint(14.668679, 120.570991),
            GeoPoint(14.668623, 120.570997),
            GeoPoint(14.668603, 120.570987),
            GeoPoint(14.668419, 120.570569),
            GeoPoint(14.668283, 120.570581),
            GeoPoint(14.668075, 120.570651),
            GeoPoint(14.667905, 120.570692),
            GeoPoint(14.667837, 120.570706),
            GeoPoint(14.667761, 120.570740),
            GeoPoint(14.667704, 120.570805),
            GeoPoint(14.667580, 120.571006),
            GeoPoint(14.667514, 120.571127),
            GeoPoint(14.667490, 120.571196),
            GeoPoint(14.667457, 120.571243),
            GeoPoint(14.667414, 120.571284),
            GeoPoint(14.667221, 120.571418),
            GeoPoint(14.665248, 120.572884),
            GeoPoint(14.663453, 120.574536),
            GeoPoint(14.662856, 120.574886),
            GeoPoint(14.647783, 120.564737),
            GeoPoint(14.643049, 120.564750),
            GeoPoint(14.639810, 120.562835),
            GeoPoint(14.635983, 120.557794),
            GeoPoint(14.631750, 120.530500),
            GeoPoint(14.624937, 120.525521),
            GeoPoint(14.610985, 120.531947),
            GeoPoint(14.596199, 120.525854),
            GeoPoint(14.588905, 120.515389),
            GeoPoint(14.589221, 120.501306),
            GeoPoint(14.591886, 120.486449),
            GeoPoint(14.598864, 120.479174),
            GeoPoint(14.623531, 120.478736),
            GeoPoint(14.627720, 120.476295),
            GeoPoint(14.628505, 120.476638),
            GeoPoint(14.630375, 120.475987)
        ),
        listOf(/* Points for polygonSamal */
            GeoPoint(14.728722, 120.437463),
            GeoPoint(14.736988, 120.440835),
            GeoPoint(14.743068, 120.445749),
            GeoPoint(14.746057, 120.448775),
            GeoPoint(14.748007, 120.453496),
            GeoPoint(14.747779, 120.455148),
            GeoPoint(14.748111, 120.456864),
            GeoPoint(14.748090, 120.458409),
            GeoPoint(14.749397, 120.461263),
            GeoPoint(14.749335, 120.463066),
            GeoPoint(14.748733, 120.464160),
            GeoPoint(14.748920, 120.465104),
            GeoPoint(14.752655, 120.468237),
            GeoPoint(14.751991, 120.469224),
            GeoPoint(14.751680, 120.471155),
            GeoPoint(14.753029, 120.475640),
            GeoPoint(14.752676, 120.477271),
            GeoPoint(14.752946, 120.478344),
            GeoPoint(14.753568, 120.478880),
            GeoPoint(14.751970, 120.482356),
            GeoPoint(14.753257, 120.485124),
            GeoPoint(14.753535, 120.486592),
            GeoPoint(14.754406, 120.487622),
            GeoPoint(14.754271, 120.489757),
            GeoPoint(14.755018, 120.493147),
            GeoPoint(14.754728, 120.494434),
            GeoPoint(14.755786, 120.495110),
            GeoPoint(14.756543, 120.498071),
            GeoPoint(14.756917, 120.504165),
            GeoPoint(14.755890, 120.504627),
            GeoPoint(14.756004, 120.505088),
            GeoPoint(14.756844, 120.505828),
            GeoPoint(14.756813, 120.506558),
            GeoPoint(14.756375, 120.507238),
            GeoPoint(14.756692, 120.514199),
            GeoPoint(14.756890, 120.518577),
            GeoPoint(14.754014, 120.520959),
            GeoPoint(14.752913, 120.522787),
            GeoPoint(14.750801, 120.526019),
            GeoPoint(14.749561, 120.529387),
            GeoPoint(14.748328, 120.531109),
            GeoPoint(14.747132, 120.532050),
            GeoPoint(14.744623, 120.536223),
            GeoPoint(14.742620, 120.538468),
            GeoPoint(14.742437, 120.539151),
            GeoPoint(14.742569, 120.540479),
            GeoPoint(14.742261, 120.540987),
            GeoPoint(14.742928, 120.542102),
            GeoPoint(14.742782, 120.544886),
            GeoPoint(14.742011, 120.546131),
            GeoPoint(14.740845, 120.549279),
            GeoPoint(14.741257, 120.549395),
            GeoPoint(14.741697, 120.549585),
            GeoPoint(14.742144, 120.549751),
            GeoPoint(14.742592, 120.549979),
            GeoPoint(14.742889, 120.550100),
            GeoPoint(14.743274, 120.550169),
            GeoPoint(14.743579, 120.550267),
            GeoPoint(14.743927, 120.550495),
            GeoPoint(14.744132, 120.550764),
            GeoPoint(14.744378, 120.551015),
            GeoPoint(14.746256, 120.552168),
            GeoPoint(14.747049, 120.553435),
            GeoPoint(14.747184, 120.553617),
            GeoPoint(14.748883, 120.554732),
            GeoPoint(14.749429, 120.555043),
            GeoPoint(14.749547, 120.555100),
            GeoPoint(14.749785, 120.555142),
            GeoPoint(14.750038, 120.555108),
            GeoPoint(14.751098, 120.554671),
            GeoPoint(14.751777, 120.554607),
            GeoPoint(14.754378, 120.552961),
            GeoPoint(14.754264, 120.552054),
            GeoPoint(14.756778, 120.550949),
            GeoPoint(14.758236, 120.550490),
            GeoPoint(14.761045, 120.549849),
            GeoPoint(14.762791, 120.549704),
            GeoPoint(14.764159, 120.549583),
            GeoPoint(14.764281, 120.549674),
            GeoPoint(14.764431, 120.549697),
            GeoPoint(14.764673, 120.549685),
            GeoPoint(14.764897, 120.549640),
            GeoPoint(14.765062, 120.549507),
            GeoPoint(14.765572, 120.549450),
            GeoPoint(14.766015, 120.549428),
            GeoPoint(14.767501, 120.549443),
            GeoPoint(14.768249, 120.549538),
            GeoPoint(14.771345, 120.550110),
            GeoPoint(14.772163, 120.550148),
            GeoPoint(14.774294, 120.551939),
            GeoPoint(14.774679, 120.551939),
            GeoPoint(14.775732, 120.551734),
            GeoPoint(14.777125, 120.551218),
            GeoPoint(14.777899, 120.550679),
            GeoPoint(14.778079, 120.550645),
            GeoPoint(14.778163, 120.550660),
            GeoPoint(14.780137, 120.551169),
            GeoPoint(14.780426, 120.549359),
            GeoPoint(14.780518, 120.549299),
            GeoPoint(14.781340, 120.549196),
            GeoPoint(14.782532, 120.548612),
            GeoPoint(14.782693, 120.548612),
            GeoPoint(14.782895, 120.548635),
            GeoPoint(14.783617, 120.548369),
            GeoPoint(14.783702, 120.548229),
            GeoPoint(14.785546, 120.547603),
            GeoPoint(14.785979, 120.547375),
            GeoPoint(14.786097, 120.547265),
            GeoPoint(14.786463, 120.547121),
            GeoPoint(14.786804, 120.547064),
            GeoPoint(14.788070, 120.546951),
            GeoPoint(14.787699, 120.545046),
            GeoPoint(14.786130, 120.543567),
            GeoPoint(14.786474, 120.541462),
            GeoPoint(14.786394, 120.540312),
            GeoPoint(14.786776, 120.531684),
            GeoPoint(14.788577, 120.524214),
            GeoPoint(14.789237, 120.522651),
            GeoPoint(14.788848, 120.520853),
            GeoPoint(14.789021, 120.518864),
            GeoPoint(14.788988, 120.517691),
            GeoPoint(14.788346, 120.516690),
            GeoPoint(14.789142, 120.514634),
            GeoPoint(14.788680, 120.512572),
            GeoPoint(14.789393, 120.509159),
            GeoPoint(14.789052, 120.507240),
            GeoPoint(14.788396, 120.505203),
            GeoPoint(14.786389, 120.502263),
            GeoPoint(14.785306, 120.498317),
            GeoPoint(14.785973, 120.495427),
            GeoPoint(14.786010, 120.493575),
            GeoPoint(14.786285, 120.491698),
            GeoPoint(14.786131, 120.490207),
            GeoPoint(14.785187, 120.488407),
            GeoPoint(14.784795, 120.486354),
            GeoPoint(14.784171, 120.485577),
            GeoPoint(14.784116, 120.484792),
            GeoPoint(14.781930, 120.483532),
            GeoPoint(14.781296, 120.480676),
            GeoPoint(14.781013, 120.480429),
            GeoPoint(14.776257, 120.478970),
            GeoPoint(14.775511, 120.477269),
            GeoPoint(14.774429, 120.476786),
            GeoPoint(14.774486, 120.475406),
            GeoPoint(14.770665, 120.472605),
            GeoPoint(14.770307, 120.470854),
            GeoPoint(14.769169, 120.469916),
            GeoPoint(14.768152, 120.468486),
            GeoPoint(14.767978, 120.466975),
            GeoPoint(14.767690, 120.465744),
            GeoPoint(14.767825, 120.463064),
            GeoPoint(14.767306, 120.461661),
            GeoPoint(14.767651, 120.460597),
            GeoPoint(14.766931, 120.459933),
            GeoPoint(14.765147, 120.459248),
            GeoPoint(14.764680, 120.457218),
            GeoPoint(14.763908, 120.455977),
            GeoPoint(14.763965, 120.454773),
            GeoPoint(14.762307, 120.454355),
            GeoPoint(14.760654, 120.452154),
            GeoPoint(14.757819, 120.450147),
            GeoPoint(14.758019, 120.448469),
            GeoPoint(14.752752, 120.443298),
            GeoPoint(14.752647, 120.441056),
            GeoPoint(14.751389, 120.439883),
            GeoPoint(14.751537, 120.437312),
            GeoPoint(14.750028, 120.436545),
            GeoPoint(14.748496, 120.433839),
            GeoPoint(14.749800, 120.430392),
            GeoPoint(14.738190, 120.422353),
            GeoPoint(14.735702, 120.416339)
        ),

        )
    lateinit var binding: ActivityMainBinding
    private lateinit var map: MapView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val markerList = mutableListOf<Marker>()
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val changeUnitNumber = 100000
    val decimalFormat = DecimalFormat("#,###.##")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        //para sa notif
        createNotificationChannel()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.bottomNavigation

        //map spinner
        val spinner: Spinner = binding.spinnerSearch
        val spinnerCategory: Spinner = binding.spinnerCategory
        val provinces = arrayOf("Bataan")
        val municipality = arrayOf(
            "Abucay", "Bagac", "Balanga", "Dinalupihan", "Hermosa",
            "Limay", "Mariveles", "Morong", "Orani", "Orion", "Pilar", "Samal"
        )
        val polygons: MutableList<Polygon> = ArrayList()
        val category = arrayOf("Overall", "House", "Vehicle", "Travel", "Food")

        val arrayMapPointers = arrayOf(
            arrayOf(14.726758, 120.492049),
            arrayOf(14.571749, 120.421139),
            arrayOf(14.675901, 120.525133),
            arrayOf(14.873402, 120.428707),
            arrayOf(14.833435, 120.481804),
            arrayOf(14.538396, 120.582241),
            arrayOf(14.453851, 120.492951),
            arrayOf(14.683001, 120.302503),
            arrayOf(14.805175, 120.516156),
            arrayOf(14.616209, 120.574393),
            arrayOf(14.666790, 120.553064),
            arrayOf(14.770519, 120.538129)
        )
        spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, category)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)


        auth = Firebase.auth
        val user = auth.currentUser

        user?.let {
            FirebaseQuery().readUserInfo(it) { user ->
                binding.lblUser.text = "Hello, " + user.firstName
            }
        }
        val tonsConverter = 1000
        val tonsUnit = "mt CO2e"
        binding.spinnerDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(

                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                user?.let {
                    binding.layoutLoading.visibility = View.VISIBLE
                    FirebaseQuery().readUserMonthlyEmission(it, year, month) { emission ->
                        FirebaseQuery().readDailyEmissions(it, year, month) { daily ->
                            FirebaseQuery().readUserYearlyEmission(it, year) { monthly ->
                                FirebaseQuery().readYearlyEmission(
                                    it,
                                    year
                                ) { monthlyEmission ->
                                    var emissionArray: Array<Array<String>>
                                    var emissionData: Array<String> = Array(4) { "" }
                                    if (position == 0) {
                                        //Monthly Emission data
                                        emissionArray =
                                            FirebaseQuery().populateDailyEmissionArray(daily)
                                        emissionData[0] = emission.houseEmission.toString()
                                        emissionData[1] = emission.foodEmission.toString()
                                        emissionData[2] = emission.vehicleEmission.toString()
                                        emissionData[3] = emission.travelEmission.toString()

                                    } else {
                                        //Yearly Emission Data
                                        emissionArray =
                                            FirebaseQuery().populateYearlyEmissionArray(
                                                monthlyEmission
                                            )
                                        emissionData[0] = monthly.houseEmission.toString()
                                        emissionData[1] = monthly.foodEmission.toString()
                                        emissionData[2] = monthly.vehicleEmission.toString()
                                        emissionData[3] = monthly.travelEmission.toString()
                                    }
                                    val dayEmission = String.format(
                                        "%.2f",
                                        (emissionData[0].toDouble() + emissionData[1].toDouble() + emissionData[2].toDouble() + emissionData[3].toDouble())
                                    ).toDouble()

                                    val array = arrayOf(
                                        arrayOf(
                                            "House",
                                            emissionData[0],
                                            percentageFormat((emissionData[0].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier(
                                                "new_icon_home",
                                                "mipmap",
                                                getPackageName()
                                            ).toString()
                                        ),
                                        arrayOf(
                                            "Food",
                                            emissionData[1],
                                            percentageFormat((emissionData[1].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier(
                                                "new_icon_food",
                                                "mipmap",
                                                getPackageName()
                                            ).toString()
                                        ),
                                        arrayOf(
                                            "Travel",
                                            emissionData[3],
                                            percentageFormat((emissionData[3].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier(
                                                "new_icon_travel",
                                                "mipmap",
                                                getPackageName()
                                            ).toString()
                                        ),
                                        arrayOf(
                                            "Vehicle",
                                            emissionData[2],
                                            percentageFormat((emissionData[2].toDouble() / dayEmission) * 100),
                                            getResources().getIdentifier(
                                                "new_icon_vehicle",
                                                "mipmap",
                                                getPackageName()
                                            ).toString()
                                        )
                                    )

                                    array.sortByDescending { it[1].toDouble() }

                                    val high = ContextCompat.getColor(
                                        this@MainActivity,
                                        com.example.carbonfootprint.R.color.high
                                    )

                                    val context = applicationContext

                                    val category1 = array[0]
                                    val category2 = array[1]
                                    val category3 = array[2]
                                    val category4 = array[3]
                                    val progress1 = category1[2].toDouble().toInt()
                                    val progress2 = category2[2].toDouble().toInt()
                                    val progress3 = category3[2].toDouble().toInt()
                                    val progress4 = category4[2].toDouble().toInt()
                                    binding.progressBarNumber1.progressTintList =
                                        ColorStateList.valueOf(high)

                                    val changeUnit = dayEmission > 100000

                                    if (changeUnit) {
                                        binding.txtMonthlyCarbonFootprint.text =
                                            decimalFormat.format(dayEmission / tonsConverter)
                                        binding.txtMonthlyCarbonFootprintUnit.text = tonsUnit
                                    } else {
                                        binding.txtMonthlyCarbonFootprint.text =
                                            decimalFormat.format(dayEmission)
                                    }

                                    if (category1[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber1.text = category1[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber1.text =
                                                decimalFormat.format(category1[1].toDouble() / tonsConverter)
                                            binding.txtEmissionUnitNumber1.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber1.text =
                                                decimalFormat.format(category1[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber1.setImageResource(category1[3].toInt())
                                        binding.progressBarNumber1.progress = progress1
                                        binding.txtEmissionPercentageNumber1.text =
                                            category1[2] + "%"

                                        if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category1[0],
                                                "Total",
                                                category1[1].toFloat(),
                                                context
                                            )
                                        ) {
                                            binding.txtEmissionNameNumber1.setTextColor(high)
                                            binding.txtEmissionEmissionNumber1.setTextColor(high)
                                            binding.progressBarNumber1.progressTintList =
                                                ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber1.setTextColor(high)
                                            binding.txtEmissionPercentageNumber1.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber1.visibility = View.GONE
                                    }

                                    if (category2[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber2.text = category2[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber2.text =
                                                decimalFormat.format(
                                                    category2[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber2.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber2.text =
                                                decimalFormat.format(category2[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber2.setImageResource(category2[3].toInt())
                                        binding.progressBarNumber2.progress = progress2
                                        binding.txtEmissionPercentageNumber2.text =
                                            category2[2] + "%"

                                        if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category2[0],
                                                "Total",
                                                category2[1].toFloat(),
                                                context
                                            )
                                        ) {
                                            binding.txtEmissionNameNumber2.setTextColor(high)
                                            binding.txtEmissionEmissionNumber2.setTextColor(high)
                                            binding.progressBarNumber2.progressTintList =
                                                ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber2.setTextColor(high)
                                            binding.txtEmissionPercentageNumber2.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber2.visibility = View.GONE
                                    }

                                    if (category3[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber3.text = category3[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber3.text =
                                                decimalFormat.format(
                                                    category3[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber3.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber3.text =
                                                decimalFormat.format(category3[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber3.setImageResource(category3[3].toInt())
                                        binding.progressBarNumber3.progress = progress3
                                        binding.txtEmissionPercentageNumber3.text =
                                            category3[2] + "%"

                                        if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category3[0],
                                                "Total",
                                                category3[1].toFloat(),
                                                context
                                            )
                                        ) {
                                            binding.txtEmissionNameNumber3.setTextColor(high)
                                            binding.txtEmissionEmissionNumber3.setTextColor(high)
                                            binding.progressBarNumber3.progressTintList =
                                                ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber3.setTextColor(high)
                                            binding.txtEmissionPercentageNumber3.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber3.visibility = View.GONE
                                    }

                                    if (category4[1].toDouble() > 0.0) {
                                        binding.txtEmissionNameNumber4.text = category4[0]
                                        if (changeUnit) {
                                            binding.txtEmissionEmissionNumber4.text =
                                                decimalFormat.format(
                                                    category4[1].toDouble() / tonsConverter
                                                )
                                            binding.txtEmissionUnitNumber4.text = tonsUnit
                                        } else {
                                            binding.txtEmissionEmissionNumber4.text =
                                                decimalFormat.format(category4[1].toDouble())
                                        }
                                        binding.txtEmissionIconNumber4.setImageResource(category4[3].toInt())
                                        binding.progressBarNumber4.progress = progress4
                                        binding.txtEmissionPercentageNumber4.text =
                                            category4[2] + "%"

                                        if (EmissionLevelIdentifier().emissionLevelIdentifier(
                                                category4[0],
                                                "Total",
                                                category4[1].toFloat(),
                                                context
                                            )
                                        ) {
                                            binding.txtEmissionNameNumber4.setTextColor(high)
                                            binding.txtEmissionEmissionNumber4.setTextColor(high)
                                            binding.progressBarNumber4.progressTintList =
                                                ColorStateList.valueOf(high)
                                            binding.txtEmissionUnitNumber4.setTextColor(high)
                                            binding.txtEmissionPercentageNumber4.setTextColor(high)
                                        }
                                    } else {
                                        binding.layoutEmissionNumber4.visibility = View.GONE
                                    }


                                    val barChart = binding.barChart
                                    barChart.animateY(1500)
                                    val entries = ArrayList<BarEntry>()
                                    barChart.getAxisLeft().setDrawGridLines(false)
                                    barChart.setScaleEnabled(false)
                                    barChart.setHighlightPerTapEnabled(false)
                                    barChart.setHighlightPerDragEnabled(false)
                                    barChart.getAxisRight().setDrawGridLines(false)
                                    barChart.getXAxis().setDrawGridLines(false)
                                    barChart.axisRight.isEnabled = false
                                    barChart.setFitBars(false)
                                    barChart.legend.isEnabled = false
                                    barChart.description.isEnabled = false
                                    barChart.getXAxis().setLabelCount(31)
                                    barChart.xAxis.textSize = 10f

                                    emissionArray.forEachIndexed { index, data ->
                                        val totalEmission =
                                            data[1].toFloat() + data[2].toFloat() + data[3].toFloat() + data[4].toFloat()

                                        if (changeUnit) {
                                            if (totalEmission > 0.0f) {
                                                entries.add(
                                                    BarEntry(
                                                        index.toFloat(),
                                                        totalEmission / tonsConverter
                                                    )
                                                )
                                            }
                                        } else {
                                            if (totalEmission > 0.0f) {
                                                entries.add(
                                                    BarEntry(
                                                        index.toFloat(),
                                                        totalEmission
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    val xAxisLabelDays = ArrayList<String>()
                                    var unit: String
                                    for (i in 1..31) {
                                        if (i % 10 == 1) {
                                            unit = "st day"
                                        } else if (i % 10 == 2) {
                                            unit = "nd day"
                                        } else if (i % 10 == 3) {
                                            unit = "rd day"
                                        } else {
                                            unit = "th day"
                                        }
                                        xAxisLabelDays.add("${i}${unit}")
                                    }

                                    //for the 12 months of the year
                                    val xAxisLabel = ArrayList<String>()
                                    xAxisLabel.add("Jan")
                                    xAxisLabel.add("Feb")
                                    xAxisLabel.add("Mar")
                                    xAxisLabel.add("Apr")
                                    xAxisLabel.add("May")
                                    xAxisLabel.add("Jun")
                                    xAxisLabel.add("Jul")
                                    xAxisLabel.add("Aug")
                                    xAxisLabel.add("Sep")
                                    xAxisLabel.add("Oct")
                                    xAxisLabel.add("Nov")
                                    xAxisLabel.add("Dec")

                                    val xAxis: XAxis = barChart.getXAxis()
                                    val yAxis: YAxis = barChart.axisLeft
                                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                                    val xFormatter: ValueFormatter = object : ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            val index = value.toInt()
                                            val selectedLabelArray =
                                                if (binding.spinnerDate.selectedItem == "Month") xAxisLabelDays else xAxisLabel
                                            return if (index >= 0 && index < selectedLabelArray.size) {
                                                selectedLabelArray[index]
                                            } else {
                                                ""
                                            }
                                        }
                                    }

                                    xAxis.granularity = 1f
                                    xAxis.yOffset = -2f
                                    xAxis.valueFormatter = xFormatter

                                    val yFormatter = LargeValueFormatter()

                                    yAxis.valueFormatter = yFormatter


                                    val barDataSet = BarDataSet(entries, "Bar Data")
                                    val colors = arrayListOf(
                                        Color.parseColor("#66c2a4")
                                    )

                                    val dataFormatter: LargeValueFormatter = object : LargeValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            return if (value < 1) {
                                                DecimalFormat("0.###").format(value)
                                            } else {
                                                super.getFormattedValue(value)
                                            }
                                        }
                                    }

                                    barDataSet.colors = colors
                                    val data = BarData(barDataSet)
                                    data.setValueTextSize(10f)
                                    data.setBarWidth(0.5f)
                                    data.setValueFormatter(dataFormatter)

                                    barChart.data = data

                                    barChart.setFitBars(false)
                                    barChart.invalidate()


                                    binding.layoutLoading.visibility = View.GONE
                                    binding.layoutReport.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        bottomNavigationView.selectedItemId = R.id.menu_item2
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            // Handle item selection
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    binding.layoutReport.visibility = View.GONE
                    binding.layoutProfile.visibility = View.GONE
                    binding.mapLayout.visibility = View.VISIBLE
                    true
                }

                R.id.menu_item2 -> {
                    binding.layoutReport.visibility = View.VISIBLE
                    binding.layoutProfile.visibility = View.GONE
                    binding.mapLayout.visibility = View.GONE
                    true
                }

                R.id.menu_item3 -> {
                    binding.layoutReport.visibility = View.GONE
                    binding.layoutProfile.visibility = View.VISIBLE
                    binding.mapLayout.visibility = View.GONE
                    binding.scrollViewProfile.visibility = View.VISIBLE
                    binding.scrollViewProfileEditNotification.visibility = View.GONE
                    binding.scrollViewProfileEditPassword.visibility = View.GONE
                    true
                }

                else -> false
            }
        }

        binding.layoutEmissionNumber1.setOnClickListener {
            openCategory(category = binding.txtEmissionNameNumber1.text.toString())
        }
        binding.layoutEmissionNumber2.setOnClickListener {
            openCategory(category = binding.txtEmissionNameNumber2.text.toString())
        }
        binding.layoutEmissionNumber3.setOnClickListener {
            openCategory(category = binding.txtEmissionNameNumber3.text.toString())
        }
        binding.layoutEmissionNumber4.setOnClickListener {
            openCategory(category = binding.txtEmissionNameNumber4.text.toString())
        }
        binding.btnReportShowReccommendation.setOnClickListener() {
            val intent = Intent(this, RecommendationMainActivity::class.java)
            startActivity(intent)
        }
        binding.btnDailyCalculation.setOnClickListener() {
            val intent = Intent(this, DailyCheckUp::class.java)
            startActivity(intent)
        }
        binding.rowEditProfile.setOnClickListener() {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
        binding.rowEditPassword.setOnClickListener() {
            binding.scrollViewProfile.visibility = View.GONE
            binding.scrollViewProfileEditPassword.visibility = View.VISIBLE
        }
        binding.buttonChangePassword.setOnClickListener() {
            Toast.makeText(this, "Password change successfully", Toast.LENGTH_SHORT).show()
            binding.scrollViewProfile.visibility = View.VISIBLE
            binding.scrollViewProfileEditPassword.visibility = View.GONE
        }
        binding.rowNotification.setOnClickListener() {
            binding.scrollViewProfile.visibility = View.GONE
            binding.scrollViewProfileEditNotification.visibility = View.VISIBLE
            checkPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                MainActivity.NOTIFICATION_PERMISSION_CODE
            )
        }
        binding.btnSetNotificationTime.setOnClickListener() {
            scheduleNotification()
        }
        binding.rowLogOut.setOnClickListener() {
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        //for map search option
        binding.switchLabelIconVisibility.setOnClickListener {
            if (binding.switchLabelIconVisibility.isChecked) {
                Log.d("switchtest", "on")
                for (overlay in map.overlays) {
                    if (overlay is Marker) {
                        map.overlays.remove(overlay)
                    }
                }
                map.invalidate()
            } else {
                if (binding.rbtnProvince.isChecked) {
                    addMarker(14.671255, 120.444681, "Bataan");
                } else {
                    addMarker(14.726758, 120.492049, "Abucay");
                    addMarker(14.571749, 120.421139, "Bagac");
                    addMarker(14.675901, 120.525133, "Balanga");
                    addMarker(14.873402, 120.428707, "Dinalupihan");
                    addMarker(14.833435, 120.481804, "Hermosa");
                    addMarker(14.538396, 120.582241, "Limay");
                    addMarker(14.453851, 120.492951, "Mariveles");
                    addMarker(14.683001, 120.302503, "Morong");
                    addMarker(14.805175, 120.516156, "Orani");
                    addMarker(14.616209, 120.574393, "Orion");
                    addMarker(14.666790, 120.553064, "Pilar");
                    addMarker(14.770519, 120.538129, "Samal");
                }
                map.invalidate()
            }
        }
        binding.rbtnProvince.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (overlay in map.overlays) {
                    map.getOverlays().clear()
                    map.invalidate()

                }
                //start point of the map when loading in
                val changePoint = GeoPoint(14.671255, 120.444681);
                map.controller.setCenter(changePoint);
                spinner.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
                map.controller.setZoom(11)

                user?.let {
                    FirebaseQuery().readUserProfile(user.uid) { profile ->
                        val province = profile.province


                        val provinceIndex =
                            (0 until spinner.adapter.count).firstOrNull { spinner.adapter.getItem(it) == province }
                                ?: 0

                        binding.spinnerSearch.setSelection(provinceIndex)
                        var selectedDate = binding.switchLabelIconDate.isChecked
                        val selectedCategory = binding.spinnerCategory.selectedItem.toString()
                        FirebaseQuery().readMunicipalityTypeEmission(selectedCategory, selectedDate, year, month) { emission ->
                            //peak 1st year student code
                            val municipalityBataanCarbonTotal =
                                arrayOf(
                                    emission.abucayEmission,
                                    emission.bagacEmission,
                                    emission.balangaEmission,
                                    emission.dinalupihanEmission,
                                    emission.hermosaEmission,
                                    emission.limayEmission,
                                    emission.marivelesEmission,
                                    emission.morongEmission,
                                    emission.oraniEmission,
                                    emission.orionEmission,
                                    emission.pilarEmission,
                                    emission.samalEmission
                                )

                            var bataanTotalCarbon = 0.0

                            municipalityBataanCarbonTotal.forEach {
                                bataanTotalCarbon += it
                            }

                            val average = 5000000.0

                            val percent = emissionLocationLevelIdentifier(
                                average = average,
                                emission = bataanTotalCarbon
                            )

                            val polygonPoints = Polygon()
                            polygonPoints.points = LocationData().polygonBataan

                            val color0Percent = Color.parseColor("#31b470")
                            val color100Percent = Color.parseColor("#de2d26")

                            val interpolatedColor = ArgbEvaluator().evaluate(
                                percent,
                                color0Percent,
                                color100Percent
                            ) as Int

                            polygonPoints.fillPaint.color = interpolatedColor
                            polygonPoints.outlinePaint.color =
                                ContextCompat.getColor(this@MainActivity, R.color.black)
                            polygonPoints.outlinePaint.setStrokeWidth(2f);
                            polygons.add(polygonPoints)
                            map.overlays.add(polygonPoints)

                            addMarker(14.671255, 120.444681, "Bataan");
                            map.invalidate()
                        }
                    }
                }


            }
            if (binding.switchLabelIconVisibility.isChecked) {
                Log.d("switchtest", "on")
                for (overlay in map.overlays) {
                    if (overlay is Marker) {
                        map.overlays.remove(overlay)
                    }
                }
            }
        }

        binding.switchLabelIconDate.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(binding.rbtnProvince.isChecked){
                for (overlay in map.overlays) {
                    map.getOverlays().clear()
                    map.invalidate()
                }
                //start point of the map when loading in
                val changePoint = GeoPoint(14.671255, 120.444681);
                map.controller.setCenter(changePoint);
                spinner.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
                map.controller.setZoom(11)

                user?.let {
                    FirebaseQuery().readUserProfile(user.uid) { profile ->
                        val province = profile.province


                        val provinceIndex =
                            (0 until spinner.adapter.count).firstOrNull { spinner.adapter.getItem(it) == province }
                                ?: 0

                        binding.spinnerSearch.setSelection(provinceIndex)
                        var selectedDate = binding.switchLabelIconDate.isChecked
                        val selectedCategory = binding.spinnerCategory.selectedItem.toString()
                        FirebaseQuery().readMunicipalityTypeEmission(selectedCategory, selectedDate, year, month) { emission ->
                            //peak 1st year student code
                            val municipalityBataanCarbonTotal =
                                arrayOf(
                                    emission.abucayEmission,
                                    emission.bagacEmission,
                                    emission.balangaEmission,
                                    emission.dinalupihanEmission,
                                    emission.hermosaEmission,
                                    emission.limayEmission,
                                    emission.marivelesEmission,
                                    emission.morongEmission,
                                    emission.oraniEmission,
                                    emission.orionEmission,
                                    emission.pilarEmission,
                                    emission.samalEmission
                                )

                            var bataanTotalCarbon = 0.0

                            municipalityBataanCarbonTotal.forEach {
                                bataanTotalCarbon += it
                            }

                            val average = 5000000.0

                            val percent = emissionLocationLevelIdentifier(
                                average = average,
                                emission = bataanTotalCarbon
                            )

                            val polygonPoints = Polygon()
                            polygonPoints.points = LocationData().polygonBataan

                            val color0Percent = Color.parseColor("#31b470")
                            val color100Percent = Color.parseColor("#de2d26")

                            val interpolatedColor = ArgbEvaluator().evaluate(
                                percent,
                                color0Percent,
                                color100Percent
                            ) as Int

                            polygonPoints.fillPaint.color = interpolatedColor
                            polygonPoints.outlinePaint.color =
                                ContextCompat.getColor(this@MainActivity, R.color.black)
                            polygonPoints.outlinePaint.setStrokeWidth(2f);
                            polygons.add(polygonPoints)
                            map.overlays.add(polygonPoints)

                            addMarker(14.671255, 120.444681, "Bataan");
                            map.invalidate()
                        }
                    }
                }

            }
            if (binding.switchLabelIconVisibility.isChecked) {
                Log.d("switchtest", "on")
                for (overlay in map.overlays) {
                    if (overlay is Marker) {
                        map.overlays.remove(overlay)
                    }
                }
            }
            if(binding.rbtnMunicipality.isChecked){
                for (overlay in map.overlays) {
                    map.getOverlays().clear()
                    map.invalidate()
                }
                spinner.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, municipality)
                user?.let {
                    FirebaseQuery().readUserProfile(user.uid) { profile ->
                        val municipality = profile.municipality

                        val municipalityIndex =
                            (0 until spinner.adapter.count).firstOrNull { spinner.adapter.getItem(it) == municipality }
                                ?: 0

                        binding.spinnerSearch.setSelection(municipalityIndex)
                        binding.spinnerCategory.setSelection(0)
                    }
                    val selectedDate = binding.switchLabelIconDate.isChecked
                    val selectedCategory = binding.spinnerCategory.selectedItem.toString()
                    FirebaseQuery().readMunicipalityTypeEmission(
                        selectedCategory,
                        selectedDate,
                        year,
                        month
                    ) { emission ->
                        //peak 1st year student code
                        val municipalityBataanCarbonTotal =
                            arrayOf(
                                emission.abucayEmission,
                                emission.bagacEmission,
                                emission.balangaEmission,
                                emission.dinalupihanEmission,
                                emission.hermosaEmission,
                                emission.limayEmission,
                                emission.marivelesEmission,
                                emission.morongEmission,
                                emission.oraniEmission,
                                emission.orionEmission,
                                emission.pilarEmission,
                                emission.samalEmission
                            )

                        var bataanTotalCarbon = 0.0

                        var index = 0

                        municipalityBataanCarbonTotal.forEach {
                            bataanTotalCarbon += it
                            if (it > 0) {
                                index++
                            }
                        }

                        val average = bataanTotalCarbon / index

                        val municipalityBataanCarbonpercentage =
                            arrayOf(
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.abucayEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.bagacEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.balangaEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.dinalupihanEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.hermosaEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.limayEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.marivelesEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.morongEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.oraniEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.orionEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.pilarEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.samalEmission
                                )
                            )

                        for (polygon in polygons) {
                            map.overlays.remove(polygon)
                        }

                        for (x in 0..11) {
                            val percent = municipalityBataanCarbonpercentage[x]
                            val polygonPoints = Polygon()
                            polygonPoints.points = polygonNames[x]

                            val color0Percent = Color.parseColor("#31b470")
                            val color100Percent = Color.parseColor("#de2d26")

                            val interpolatedColor = ArgbEvaluator().evaluate(
                                percent,
                                color0Percent,
                                color100Percent
                            ) as Int

                            polygonPoints.fillPaint.color = interpolatedColor
                            polygonPoints.outlinePaint.color =
                                ContextCompat.getColor(this@MainActivity, R.color.black)
                            polygonPoints.outlinePaint.setStrokeWidth(2f);
                            polygons.add(polygonPoints)
                            map.overlays.add(polygonPoints)
                            addMarker(14.726758, 120.492049, "Abucay");
                            addMarker(14.571749, 120.421139, "Bagac");
                            addMarker(14.675901, 120.525133, "Balanga");
                            addMarker(14.873402, 120.428707, "Dinalupihan");
                            addMarker(14.833435, 120.481804, "Hermosa");
                            addMarker(14.538396, 120.582241, "Limay");
                            addMarker(14.453851, 120.492951, "Mariveles");
                            addMarker(14.683001, 120.302503, "Morong");
                            addMarker(14.805175, 120.516156, "Orani");
                            addMarker(14.616209, 120.574393, "Orion");
                            addMarker(14.666790, 120.553064, "Pilar");
                            addMarker(14.770519, 120.538129, "Samal");
                            map.invalidate()
                        }
                    }
                }
            }
        }

        binding.rbtnMunicipality.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (overlay in map.overlays) {
                    map.getOverlays().clear()
                    map.invalidate()
                }
                spinner.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, municipality)
                user?.let {
                    FirebaseQuery().readUserProfile(user.uid) { profile ->
                        val municipality = profile.municipality

                        val municipalityIndex =
                            (0 until spinner.adapter.count).firstOrNull { spinner.adapter.getItem(it) == municipality }
                                ?: 0

                        binding.spinnerSearch.setSelection(municipalityIndex)
                        binding.spinnerCategory.setSelection(0)
                    }
                    val selectedDate = binding.switchLabelIconDate.isChecked
                    val selectedCategory = binding.spinnerCategory.selectedItem.toString()
                    FirebaseQuery().readMunicipalityTypeEmission(selectedCategory, selectedDate, year, month) { emission ->
                        //peak 1st year student code
                        val municipalityBataanCarbonTotal =
                            arrayOf(
                                emission.abucayEmission,
                                emission.bagacEmission,
                                emission.balangaEmission,
                                emission.dinalupihanEmission,
                                emission.hermosaEmission,
                                emission.limayEmission,
                                emission.marivelesEmission,
                                emission.morongEmission,
                                emission.oraniEmission,
                                emission.orionEmission,
                                emission.pilarEmission,
                                emission.samalEmission
                            )

                        var bataanTotalCarbon = 0.0

                        var index = 0

                        municipalityBataanCarbonTotal.forEach {
                            bataanTotalCarbon += it
                            if (it > 0) {
                                index++
                            }
                        }

                        val average = bataanTotalCarbon / index

                        val municipalityBataanCarbonpercentage =
                            arrayOf(
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.abucayEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.bagacEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.balangaEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.dinalupihanEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.hermosaEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.limayEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.marivelesEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.morongEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.oraniEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.orionEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.pilarEmission
                                ),
                                emissionLocationLevelIdentifier(
                                    average = average,
                                    emission = emission.samalEmission
                                )
                            )

                        for (polygon in polygons) {
                            map.overlays.remove(polygon)
                        }

                        for (x in 0..11) {
                            val percent = municipalityBataanCarbonpercentage[x]
                            val polygonPoints = Polygon()
                            polygonPoints.points = polygonNames[x]

                            val color0Percent = Color.parseColor("#31b470")
                            val color100Percent = Color.parseColor("#de2d26")

                            val interpolatedColor = ArgbEvaluator().evaluate(
                                percent,
                                color0Percent,
                                color100Percent
                            ) as Int

                            polygonPoints.fillPaint.color = interpolatedColor
                            polygonPoints.outlinePaint.color =
                                ContextCompat.getColor(this@MainActivity, R.color.black)
                            polygonPoints.outlinePaint.setStrokeWidth(2f);
                            polygons.add(polygonPoints)
                            map.overlays.add(polygonPoints)
                            addMarker(14.726758, 120.492049, "Abucay");
                            addMarker(14.571749, 120.421139, "Bagac");
                            addMarker(14.675901, 120.525133, "Balanga");
                            addMarker(14.873402, 120.428707, "Dinalupihan");
                            addMarker(14.833435, 120.481804, "Hermosa");
                            addMarker(14.538396, 120.582241, "Limay");
                            addMarker(14.453851, 120.492951, "Mariveles");
                            addMarker(14.683001, 120.302503, "Morong");
                            addMarker(14.805175, 120.516156, "Orani");
                            addMarker(14.616209, 120.574393, "Orion");
                            addMarker(14.666790, 120.553064, "Pilar");
                            addMarker(14.770519, 120.538129, "Samal");
                            map.invalidate()
                        }
                    }
                }
            }

            if (binding.switchLabelIconVisibility.isChecked) {
                Log.d("switchtest", "on")
                for (overlay in map.overlays) {
                    if (overlay is Marker) {
                        map.overlays.remove(overlay)
                    }
                }
                map.invalidate()
            }
        }

        binding.spinnerSearch.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle the selected item change here
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    for (x in 1 until municipality.size) {
                        if (selectedItem.equals(municipality[x])) {
                            Log.d("radioTest", municipality[x])
                            val changePoint =
                                GeoPoint(arrayMapPointers[x][0], arrayMapPointers[x][1]);
                            map.controller.setCenter(changePoint);
                            map.controller.setZoom(12)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where nothing is selected
                }
            }

        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle the selected item change here
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    if (binding.rbtnProvince.isChecked) {
                        FirebaseQuery().readMunicipalityTypeEmission(selectedItem,binding.switchLabelIconDate.isChecked,year, month) { emission ->
                            //peak 1st year student code
                            val municipalityBataanCarbonTotal =
                                arrayOf(
                                    emission.abucayEmission,
                                    emission.bagacEmission,
                                    emission.balangaEmission,
                                    emission.dinalupihanEmission,
                                    emission.hermosaEmission,
                                    emission.limayEmission,
                                    emission.marivelesEmission,
                                    emission.morongEmission,
                                    emission.oraniEmission,
                                    emission.orionEmission,
                                    emission.pilarEmission,
                                    emission.samalEmission
                                )

                            var bataanTotalCarbon = 0.0

                            municipalityBataanCarbonTotal.forEach {
                                bataanTotalCarbon += it
                            }

                            for (overlay in map.overlays) {
                                map.getOverlays().clear()
                                map.invalidate()
                            }

                            val average = 5000000.0

                            val percent = emissionLocationLevelIdentifier(
                                average = average,
                                emission = bataanTotalCarbon
                            )

                            val polygonPoints = Polygon()
                            polygonPoints.points = LocationData().polygonBataan

                            val color0Percent = Color.parseColor("#31b470")
                            val color100Percent = Color.parseColor("#de2d26")

                            val interpolatedColor = ArgbEvaluator().evaluate(
                                percent,
                                color0Percent,
                                color100Percent
                            ) as Int

                            polygonPoints.fillPaint.color = interpolatedColor
                            polygonPoints.outlinePaint.color =
                                ContextCompat.getColor(this@MainActivity, R.color.black)
                            polygonPoints.outlinePaint.setStrokeWidth(2f);
                            polygons.add(polygonPoints)
                            map.overlays.add(polygonPoints)
                            addMarker(14.671255, 120.444681, "Bataan");
                            map.controller.setZoom(11)
                            map.invalidate()
                        }
                    } else {
                        FirebaseQuery().readMunicipalityTypeEmission(selectedItem,binding.switchLabelIconDate.isChecked, year, month) { emission ->
                            //peak 1st year student code
                            val municipalityBataanCarbonTotal =
                                arrayOf(
                                    emission.abucayEmission,
                                    emission.bagacEmission,
                                    emission.balangaEmission,
                                    emission.dinalupihanEmission,
                                    emission.hermosaEmission,
                                    emission.limayEmission,
                                    emission.marivelesEmission,
                                    emission.morongEmission,
                                    emission.oraniEmission,
                                    emission.orionEmission,
                                    emission.pilarEmission,
                                    emission.samalEmission
                                )

                            var bataanTotalCarbon = 0.0

                            var index = 0

                            municipalityBataanCarbonTotal.forEach {
                                bataanTotalCarbon += it
                                if (it > 0) {
                                    index++
                                }
                            }

                            val average = bataanTotalCarbon / index

                            val municipalityBataanCarbonpercentage =
                                arrayOf(
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.abucayEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.bagacEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.balangaEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.dinalupihanEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.hermosaEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.limayEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.marivelesEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.morongEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.oraniEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.orionEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.pilarEmission
                                    ),
                                    emissionLocationLevelIdentifier(
                                        average = average,
                                        emission = emission.samalEmission
                                    )
                                )

                            for (polygon in polygons) {
                                map.overlays.remove(polygon)
                            }

                            for (x in 0..11) {
                                val percent = municipalityBataanCarbonpercentage[x]
                                val polygonPoints = Polygon()
                                polygonPoints.points = polygonNames[x]

                                val color0Percent = Color.parseColor("#31b470")
                                val color100Percent = Color.parseColor("#de2d26")

                                val interpolatedColor = ArgbEvaluator().evaluate(
                                    percent,
                                    color0Percent,
                                    color100Percent
                                ) as Int

                                polygonPoints.fillPaint.color = interpolatedColor
                                polygonPoints.outlinePaint.color =
                                    ContextCompat.getColor(this@MainActivity, R.color.black)
                                polygonPoints.outlinePaint.setStrokeWidth(2f);
                                polygons.add(polygonPoints)
                                map.overlays.add(polygonPoints)
                            }

                            //adding markers to all municipality
                            addMarker(14.726758, 120.492049, "Abucay");
                            addMarker(14.571749, 120.421139, "Bagac");
                            addMarker(14.675901, 120.525133, "Balanga");
                            addMarker(14.873402, 120.428707, "Dinalupihan");
                            addMarker(14.833435, 120.481804, "Hermosa");
                            addMarker(14.538396, 120.582241, "Limay");
                            addMarker(14.453851, 120.492951, "Mariveles");
                            addMarker(14.683001, 120.302503, "Morong");
                            addMarker(14.805175, 120.516156, "Orani");
                            addMarker(14.616209, 120.574393, "Orion");
                            addMarker(14.666790, 120.553064, "Pilar");
                            addMarker(14.770519, 120.538129, "Samal");

                            map.invalidate()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case where nothing is selected
                }
            }

        Configuration.getInstance().setUserAgentValue(this.getPackageName());
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller
        //zoom uhhhhhhhhhhhhhhhhhhhhhhhhhhh
        mapController.setZoom(12.5)
        //start point of the map when loading in
        val startPoint = GeoPoint(14.641684, 120.481842);
        mapController.setCenter(startPoint);
        map.invalidate();
        map.setBuiltInZoomControls(false)


        map.invalidate()

        binding.buttonChangePassword.setOnClickListener {
            if (binding.txtPassword.text.toString() == binding.txtConfirmPassword.text.toString()) {
                reauthenticateAndChangePassword(
                    binding.txtOldPassword.text.toString(),
                    binding.txtConfirmPassword.text.toString()
                )
            }
        }

    }

    private fun reauthenticateAndChangePassword(oldPassword: String, newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(user!!.email!!, oldPassword)

        // Prompt the user to re-authenticate
        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // Re-authentication successful, update the password
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updatePasswordTask ->
                            if (updatePasswordTask.isSuccessful) {
                                // Password updated successfully
                                Toast.makeText(
                                    this,
                                    "Password changed successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Password update failed
                                Toast.makeText(
                                    this,
                                    "Failed to change password: ${updatePasswordTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    // Display message if the old password is incorrect
                    Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openCategory(category: String) {
        when (category) {
            "House" -> {
                val intent = Intent(this, HouseReport::class.java)
                startActivity(intent)
            }

            "Food" -> {
                val intent = Intent(this, FoodReport::class.java)
                startActivity(intent)
            }

            "Vehicle" -> {
                val intent = Intent(this, VehicleReport::class.java)
                startActivity(intent)
            }

            "Travel" -> {
                val intent = Intent(this, TravelReport::class.java)
                startActivity(intent)
            }
        }
    }

    //Zedrick
    private fun emissionLocationLevelIdentifier(average: Double, emission: Double): Float {
        var percent = if ((emission / average) > 1) {
            1.0
        } else {
            (emission / average) - 1
        }

        percent = ((percent * 50) / 100) + 0.5

        return percent.toFloat()//returns value from 0.0f to 1.0f
    }

    private fun readUserDisplayName(user: FirebaseUser) {
        val userId = user.uid
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                binding.lblUser.setText("Hello, " + it.child("displayName").value.toString())
            }
        }
    }

    private fun fadeInAnimation(view: View) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 1000 // milliseconds
        view.startAnimation(fadeIn)

        // Set the visibility of the view to VISIBLE after the animation
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    private fun addMarker(latitude: Double, longitude: Double, title: String) {
        val markerPosition = GeoPoint(latitude, longitude)
        val marker = Marker(map)
        marker.position = markerPosition
        marker.title = title
        map.overlays.add(marker)
        //icon changer for the markers
        val customDrawable = ContextCompat.getDrawable(this, R.mipmap.icon_feedback)
        marker.icon = customDrawable
    }

    /*
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification title"
            val text = "notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply { description = text }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("example title")
            .setContentText("example text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }

    }

     */
    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Calculating time"
        val message = "time for your daily calculation :3"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(currentYear, currentMonth, currentDay)

        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    fun percentageFormat(emissionPercent: Double): String {
        if (String.format("%.2f", emissionPercent).toDouble() == 100.00) {
            return String.format("%.0f", emissionPercent)
        } else {
            return "%.2f".format(emissionPercent)
        }
    }

    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
        }
    }
}



