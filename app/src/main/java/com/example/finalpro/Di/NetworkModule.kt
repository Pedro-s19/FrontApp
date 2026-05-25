package com.example.finalpro.Di

import com.example.finalpro.Data.Local.SessionManager
import com.example.finalpro.Data.Remote.Api.AdminApi
import com.example.finalpro.Data.Remote.Api.AlertaApi
import com.example.finalpro.Data.Remote.Api.AuthApi
import com.example.finalpro.Data.Remote.Api.CategoriaApi
import com.example.finalpro.Data.Remote.Api.GamificacionApi
import com.example.finalpro.Data.Remote.Api.GastoApi
import com.example.finalpro.Data.Remote.Api.GastoRecurrenteApi
import com.example.finalpro.Data.Remote.Api.IngresoApi
import com.example.finalpro.Data.Remote.Api.MetaAhorroApi
import com.example.finalpro.Data.Remote.Api.PresupuestoApi
import com.example.finalpro.Data.Remote.Api.ReporteApi
import com.example.finalpro.Data.Remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(sessionManager: SessionManager): AuthApi =
        RetrofitClient.build(sessionManager).create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideGastoApi(sessionManager: SessionManager): GastoApi =
        RetrofitClient.build(sessionManager).create(GastoApi::class.java)

    @Provides
    @Singleton
    fun provideIngresoApi(sessionManager: SessionManager): IngresoApi =
        RetrofitClient.build(sessionManager).create(IngresoApi::class.java)

    @Provides
    @Singleton
    fun provideCategoriaApi(sessionManager: SessionManager): CategoriaApi =
        RetrofitClient.build(sessionManager).create(CategoriaApi::class.java)

    @Provides
    @Singleton
    fun providePresupuestoApi(sessionManager: SessionManager): PresupuestoApi =
        RetrofitClient.build(sessionManager).create(PresupuestoApi::class.java)

    @Provides
    @Singleton
    fun provideMetaApi(sessionManager: SessionManager): MetaAhorroApi =
        RetrofitClient.build(sessionManager).create(MetaAhorroApi::class.java)

    @Provides
    @Singleton
    fun provideReporteApi(sessionManager: SessionManager): ReporteApi =
        RetrofitClient.build(sessionManager).create(ReporteApi::class.java)

    @Provides
    @Singleton
    fun provideAdminApi(sessionManager: SessionManager): AdminApi =
        RetrofitClient.build(sessionManager).create(AdminApi::class.java)

    @Provides
    @Singleton
    fun provideAlertaApi(sessionManager: SessionManager): AlertaApi =
        RetrofitClient.build(sessionManager).create(AlertaApi::class.java)

    @Provides
    @Singleton
    fun provideGastoRecurrenteApi(sessionManager: SessionManager): GastoRecurrenteApi =
        RetrofitClient.build(sessionManager).create(GastoRecurrenteApi::class.java)

    @Provides
    @Singleton
    fun provideGamificacionApi(sessionManager: SessionManager): GamificacionApi =
        RetrofitClient.build(sessionManager).create(GamificacionApi::class.java)
}