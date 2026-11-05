package com.example.ud05cp01_oscarramireztoro

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Contenedor principal creado por código (sin XML)
        val layoutPrincipal = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            setPadding(40, 40, 40, 40)
        }

        // 2. Parámetros de diseño con separación vertical
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 30, 0, 30)
        }

        // 3. TextView: Fecha de hoy
        val tvHoy = TextView(this).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            setTextColor(Color.DKGRAY)
            this.layoutParams = layoutParams
        }

        // 4. TextView: Fecha de nacimiento elegida
        val tvNacimiento = TextView(this).apply {
            text = "Naciste...."
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            setTextColor(Color.DKGRAY)
            this.layoutParams = layoutParams
        }

        // 5. Botón para abrir el selector
        val btnFecha = Button(this).apply {
            text = "FECHA DE NACIMIENTO"
            this.layoutParams = layoutParams
        }

        // 6. TextView: Resultado de años y días
        val tvResultado = TextView(this).apply {
            text = "Calcula el número de días desde tu\nfecha de nacimiento"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            this.layoutParams = layoutParams
        }

        // 7. Obtenemos la fecha actual del sistema
        val calendarioHoy = Calendar.getInstance()
        val anioHoy = calendarioHoy.get(Calendar.YEAR)
        val mesHoy = calendarioHoy.get(Calendar.MONTH) // Los meses van de 0 a 11
        val diaHoy = calendarioHoy.get(Calendar.DAY_OF_MONTH)

        // Asignamos la fecha de hoy formateada (sumando 1 al mes)
        tvHoy.text = "Hoy es $diaHoy/${mesHoy + 1}/$anioHoy"

        // 8. Evento clic del botón para abrir el DatePickerDialog
        btnFecha.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, anioNac, mesNac, diaNac ->
                    // Mostramos la fecha seleccionada en pantalla
                    tvNacimiento.text = "Naciste el $diaNac/${mesNac + 1}/$anioNac"

                    // Creamos el calendario de la fecha de nacimiento elegida
                    val calendarioNacimiento = Calendar.getInstance().apply {
                        set(anioNac, mesNac, diaNac, 0, 0, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    // Creamos una copia de la fecha actual a las 00:00:00
                    val copiaHoy = (calendarioHoy.clone() as Calendar).apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    // Calculamos los años cumplidos
                    var anios = copiaHoy.get(Calendar.YEAR) - calendarioNacimiento.get(Calendar.YEAR)

                    // Verificamos si en el año actual ya pasó el cumpleaños
                    val ultimoCumple = (calendarioNacimiento.clone() as Calendar).apply {
                        set(Calendar.YEAR, copiaHoy.get(Calendar.YEAR))
                    }

                    if (copiaHoy.before(ultimoCumple)) {
                        anios--
                        ultimoCumple.add(Calendar.YEAR, -1)
                    }

                    // Calculamos la diferencia en milisegundos desde el último cumpleaños hasta hoy
                    val diferenciaMillis = copiaHoy.timeInMillis - ultimoCumple.timeInMillis
                    val dias = (diferenciaMillis / (1000 * 60 * 60 * 24)).toInt()

                    // Mostramos el resultado final
                    tvResultado.text = "Tienes $anios años y $dias dias"
                },
                anioHoy,
                mesHoy,
                diaHoy
            )

            datePickerDialog.show()
        }

        // 9. Añadimos las vistas al contenedor
        layoutPrincipal.addView(tvHoy)
        layoutPrincipal.addView(tvNacimiento)
        layoutPrincipal.addView(btnFecha)
        layoutPrincipal.addView(tvResultado)

        // 10. Asignamos la vista construida como contenido de la actividad
        setContentView(layoutPrincipal)
    }
}