package com.test.fitnessstudios.core.domain

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.DrivingPtsRepository
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@DisplayName("DriveUseCase")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DriveUseCaseTest {

    @Inject
    lateinit var mapsRepository: DrivingPtsRepository

    // @Inject lateinit var fakeRepository : DrivingPtsReposity

    val fakeRepository = FakeDrivingPtsRepository()

    private lateinit var driveUseCase: DriveUseCase

    @BeforeAll
    fun setup() {
        // Create a fake DrivingPtsRepository
        driveUseCase = DriveUseCase(fakeRepository)
    }

    /*@Test
    fun `getDrivePts should return a list of LatLng`() = runTest {
        val orig = LatLng(37.7749, -122.4194)
        val dest = LatLng(37.7738, -122.4136)
        val drivePts = driveUseCase.getDrivePts(orig, dest)
        assertEquals(2, drivePts.size)
        assertEquals(LatLng(37.7749, -122.4194), drivePts[0])
        assertEquals(LatLng(37.7738, -122.4136), drivePts[1])
    }*/

    @Test
    fun `getDrivePts returns expected list of LatLng`() = runTest {
        // Given
        val orig = LatLng(37.7749, -122.4194)
        val des = LatLng(37.7749, -122.4195)
        val expectedDrivePts = listOf(LatLng(37.7749, -122.4194), LatLng(37.7749, -122.4195))

        coEvery { org.json.JSONObject("") } returns JSONObject()

        // When
        val result = driveUseCase.getDrivePts(orig, des)

        // Then
        assertEquals(expectedDrivePts, result)
    }

    @Test
    fun `no op`() {
        assertEquals(2, 2)
    }

    @Test
    fun `no op 1`() {
        assertEquals(2, 2)
    }
}

class FakeDrivingPtsRepository : DrivingPtsRepository {
    override suspend fun getDrivingPts(org: String, des: String): String {
        return jsonStringDemo
    }
}

val jsonStringDemo = """
 { 
      "geocoded_waypoints" : [
          {
             "geocoder_status" : "OK",
             "place_id" : "ChIJHchEeciwj4ARXfNSmyIQULY",
             "types" : [ "establishment", "food", "point_of_interest", "store" ]
          },
          {
             "geocoder_status" : "OK",
             "place_id" : "ChIJYe61Ui23j4AR8k8V3C8z-OA",
             "types" : [ "route" ]
          }
       ],
       "routes" : [
          {
             "bounds" : {
                "northeast" : {
                   "lat" : 37.3910437,
                   "lng" : -122.0837669
                },
                "southwest" : {
                   "lat" : 37.3856608,
                   "lng" : -122.0953244
                }
             },
             "copyrights" : "Map data ©2023",
             "legs" : [
                {
                   "distance" : {
                      "text" : "0.8 mi",
                      "value" : 1257
                   },
                   "duration" : {
                      "text" : "4 mins",
                      "value" : 249
                   },
                   "end_address" : "999-985 Castro St, Mountain View, CA 94041, USA",
                   "end_location" : {
                      "lat" : 37.385996,
                      "lng" : -122.0837669
                   },
                   "start_address" : "1035 El Monte Ave, Mountain View, CA 94040, USA",
                   "start_location" : {
                      "lat" : 37.3910437,
                      "lng" : -122.0953244
                   },
                   "steps" : [
                      {
                         "distance" : {
                            "text" : "85 ft",
                            "value" : 26
                         },
                         "duration" : {
                            "text" : "1 min",
                            "value" : 31
                         },
                         "end_location" : {
                            "lat" : 37.3908688,
                            "lng" : -122.0951317
                         },
                         "html_instructions" : "Head \u003cb\u003esoutheast\u003c/b\u003e toward \u003cb\u003eEl Camino Real\u003c/b\u003e",
                         "polyline" : {
                            "points" : "_}ecFvvehV\\_@BE"
                         },
                         "start_location" : {
                            "lat" : 37.3910437,
                            "lng" : -122.0953244
                         },
                         "travel_mode" : "DRIVING"
                      },
                      {
                         "distance" : {
                            "text" : "171 ft",
                            "value" : 52
                         },
                         "duration" : {
                            "text" : "1 min",
                            "value" : 31
                         },
                         "end_location" : {
                            "lat" : 37.3906674,
                            "lng" : -122.0946507
                         },
                         "html_instructions" : "Turn \u003cb\u003eleft\u003c/b\u003e toward \u003cb\u003eEl Camino Real\u003c/b\u003e",
                         "maneuver" : "turn-left",
                         "polyline" : {
                            "points" : "}{ecFpuehVAA?C?C?C?C@C@ATSBGDKBK@I?I"
                         },
                         "start_location" : {
                            "lat" : 37.3908688,
                            "lng" : -122.0951317
                         },
                         "travel_mode" : "DRIVING"
                      },
                      {
                         "distance" : {
                            "text" : "138 ft",
                            "value" : 42
                         },
                         "duration" : {
                            "text" : "1 min",
                            "value" : 28
                         },
                         "end_location" : {
                            "lat" : 37.3909523,
                            "lng" : -122.094363
                         },
                         "html_instructions" : "Turn \u003cb\u003eleft\u003c/b\u003e toward \u003cb\u003eEl Camino Real\u003c/b\u003e",
                         "maneuver" : "turn-left",
                         "polyline" : {
                               "points" : "uzecFprehVIAIIOOIKIQ"
                         },
                         "start_location" : {
                            "lat" : 37.3906674,
                            "lng" : -122.0946507
                         },
                         "travel_mode" : "DRIVING"
                      },
                      {
                         "distance" : {
                            "text" : "0.7 mi",
                            "value" : 1088
                         },
                         "duration" : {
                            "text" : "2 mins",
                            "value" : 136
                         },
                         "end_location" : {
                            "lat" : 37.3857072,
                            "lng" : -122.0839806
                         },
                         "html_instructions" : "Turn \u003cb\u003eright\u003c/b\u003e onto \u003cb\u003eEl Camino Real\u003c/b\u003e\u003cdiv style=\"font-size:0.9em\"\u003ePass by Baskin-Robbins (on the right in 0.4 mi)\u003c/div\u003e",
                         "maneuver" : "turn-right",
                         "polyline" : {
                            "points" : "m|ecFvpehVlAuBL[LWHU~A_EXu@JWJWv@sBtAiDL]BE`@cAVo@Re@Xm@Xw@\\}@Pc@BGLa@P]Z}@nAaDBIHS|B_G\\y@Zw@FQ`BgE"
                         },
                         "start_location" : {
                            "lat" : 37.3909523,
                            "lng" : -122.094363
                         },
                         "travel_mode" : "DRIVING"
                      },
                      {
                         "distance" : {
                            "text" : "161 ft",
                            "value" : 49
                         },
                         "duration" : {
                            "text" : "1 min",
                            "value" : 23
                         },
                         "end_location" : {
                            "lat" : 37.385996,
                            "lng" : -122.0837669
                         },
                         "html_instructions" : "Turn \u003cb\u003eleft\u003c/b\u003e onto \u003cb\u003eCastro St\u003c/b\u003e",
                         "maneuver" : "turn-left",
                         "polyline" : {
                            "points" : "u{dcFzochVHQYOYAOE"
                         },
                         "start_location" : {
                            "lat" : 37.3857072,
                            "lng" : -122.0839806
                         },
                         "travel_mode" : "DRIVING"
                      }
                   ],
                   "traffic_speed_entry" : [],
                   "via_waypoint" : []
                }
             ],
             "overview_polyline" : {
                "points" : "_}ecFvvehV`@e@AE@OVUHSDU?IIAYYS]zAqCVm@pCeH~CaIfBgElAaDjC_HhDwInCcHYOYAOE"
             },
             "summary" : "El Camino Real",
             "warnings" : [],
             "waypoint_order" : []
          }
       ],
       "status" : "OK"
    }
""".trimIndent()
