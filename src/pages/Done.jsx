import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import html2canvas from 'html2canvas'
import jsPDF from 'jspdf'
import { useCv } from '../CvContext'
import CvDocument from '../components/CvDocument'

export default function Done() {
  const { cv, template, saveCv } = useCv()
  const navigate = useNavigate()
  const [saved, setSaved] = useState(false)
  const [saveError, setSaveError] = useState('')
  const [downloading, setDownloading] = useState(false)
  const cvRef = useRef(null)

  useEffect(() => {
    let active = true
    saveCv()
      .catch((err) => { if (active) setSaveError(err.message) })
      .finally(() => { if (active) setSaved(true) })
    return () => { active = false }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  async function handleDownload() {
    setDownloading(true)
    const canvas = await html2canvas(cvRef.current, { scale: 2, backgroundColor: '#ffffff' })
    const imgData = canvas.toDataURL('image/png')

    const pdf = new jsPDF('p', 'mm', 'a4')
    const pageWidth = pdf.internal.pageSize.getWidth()
    const pageHeight = pdf.internal.pageSize.getHeight()
    const imgWidth = pageWidth
    const imgHeight = (canvas.height * imgWidth) / canvas.width

    let heightLeft = imgHeight
    let position = 0

    pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight)
    heightLeft -= pageHeight

    while (heightLeft > 0) {
      position -= pageHeight
      pdf.addPage()
      pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight)
      heightLeft -= pageHeight
    }

    pdf.save(`${cv.name || 'cv'}.pdf`)
    setDownloading(false)
  }

  function handleFinish() {
    navigate('/choice')
  }

  function handleCompareSkills() {
    navigate('/skills', { state: { from: 'done' } })
  }

  if (!saved) {
    return (
      <div className="page centered">
        <div className="card">
          <p>Saving your information…</p>
        </div>
      </div>
    )
  }

  return (
    <div className="page">
      <h1>All Set!</h1>
      {saveError ? (
        <p className="error">Could not save to the server: {saveError} Your download below will still work from this device.</p>
      ) : (
        <p className="subtitle">Your CV has been saved. Here's the final result.</p>
      )}

      <CvDocument ref={cvRef} cv={cv} template={template} />

      <div className="finalize-question">
        <div className="actions">
          <button className="btn btn-primary" onClick={handleDownload} disabled={downloading}>
            {downloading ? 'Preparing PDF…' : 'Download CV'}
          </button>
          <button className="btn btn-secondary" onClick={handleCompareSkills}>
            Compare Skills
          </button>
          <button className="btn btn-secondary" onClick={handleFinish}>
            Finish
          </button>
        </div>
      </div>
    </div>
  )
}
