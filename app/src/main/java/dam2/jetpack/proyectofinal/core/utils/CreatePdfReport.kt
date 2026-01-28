package dam2.jetpack.proyectofinal.core.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfUtils {

    fun createPdfReport(
        context: Context,
        report: String,
        fileName: String = "informe_eventos.pdf"
    ): File {
        val pdfDocument = PdfDocument()
        val paint = Paint().apply { textSize = 12f }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val lines = report.split("\n")
        var y = 40f
        val x = 40f
        val lineHeight = 18f

        lines.forEach { line ->
            if (y > 800f) return@forEach
            canvas.drawText(line, x, y, paint)
            y += lineHeight
        }

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return file
    }
}
