package com.binar.finalproject.viewmodel

import com.binar.finalproject.model.getdetailflight.PostDataFlight
import com.binar.finalproject.model.getdetailflight.datadetailflight.ResponseDetailFlight
import com.binar.finalproject.model.searchflight.PostSearchFlight
import com.binar.finalproject.model.searchflight.ResponseDataFlight
import com.binar.finalproject.network.RestfulApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import io.mockk.verify
import retrofit2.Call

class FlightViewModelTest{

    private lateinit var api : RestfulApi

    @Before
    fun setup(){
        api = mockk()
    }

    @Test
    fun testRetriveDataFlight() : Unit = runBlocking {
        val responseRetrive = mockk<Call<ResponseDataFlight>>()

        //membuat objek palsu (mock) responseRetrive dari kelas <Call<ResponseDataFilm>>
        //Objek palsu ini akan digunakan sebagai respons palsu dari pemanggilan api.getAllFilmPopular().

        every {
            runBlocking {
                api.getSearchDataFlight(PostSearchFlight("aaaa","aaaa","aaaa","aaaa","aaaa"), mapOf())
            }
        } returns responseRetrive
        val result = api.getSearchDataFlight(PostSearchFlight("aaaa","aaaa","aaaa","aaaa","aaaa"), mapOf())

        //verify, kita memastikan bahwa metode api.getAllFilmPopular() benar-benar dipanggil dengan argumen yang sesuai.

        verify {
            runBlocking {
                api.getSearchDataFlight(PostSearchFlight("aaaa","aaaa","aaaa","aaaa","aaaa"), mapOf())
            }
        }
        assertEquals(result,responseRetrive)
    }

    @Test
    fun testRetriveDataDetailFlight() : Unit = runBlocking {
        val responseRetrive = mockk<Call<ResponseDetailFlight>>()

        //membuat objek palsu (mock) responseRetrive dari kelas <Call<ResponseDataFilm>>
        //Objek palsu ini akan digunakan sebagai respons palsu dari pemanggilan api.getAllFilmPopular().

        every {
            runBlocking {
                api.getDetailFlight(PostDataFlight(1,2,3, listOf(1,2)))
            }
        } returns responseRetrive
        val result = api.getDetailFlight(PostDataFlight(1,2,3, listOf(1,2)))

        //verify, kita memastikan bahwa metode api.getAllFilmPopular() benar-benar dipanggil dengan argumen yang sesuai.

        verify {
            runBlocking {
                api.getDetailFlight(PostDataFlight(1,2,3, listOf(1,2)))
            }
        }
        assertEquals(result,responseRetrive)
    }


}