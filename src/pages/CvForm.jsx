import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useCv } from '../CvContext'
import { DropdownMultiSelect, DropdownSelect } from '../components/Dropdown'

const LOCATION_OPTIONS = [
  'Thimphu', 'Phuentsholing', 'Paro', 'Punakha', 'Gelephu', 'Samdrup Jongkhar', 'Mongar', 'Trashigang',
]

const LANGUAGE_OPTIONS = ['English', 'Dzongkha', 'Hindi', 'Nepali', 'Sharchop', 'Mandarin']

const SKILL_OPTIONS = [
  'Communication', 'Teamwork', 'Problem Solving', 'Leadership', 'Project Management',
  'Microsoft Office', 'JavaScript', 'React', 'Python', 'SQL', 'Git', 'Figma',
]

function parseList(value) {
  return value.split(',').map((v) => v.trim()).filter(Boolean)
}

export default function CvForm() {
  const { cv, setCv, saveCv } = useCv()
  const navigate = useNavigate()
  const [saving, setSaving] = useState(false)
  const [saveError, setSaveError] = useState('')

  const [locationIsOther, setLocationIsOther] = useState(
    () => !!cv.location && !LOCATION_OPTIONS.includes(cv.location)
  )
  const [customLanguages, setCustomLanguages] = useState(
    () => parseList(cv.languages).filter((l) => !LANGUAGE_OPTIONS.includes(l)).join(', ')
  )
  const [customSkills, setCustomSkills] = useState(
    () => parseList(cv.skills).filter((s) => !SKILL_OPTIONS.includes(s)).join(', ')
  )

  function update(field) {
    return (e) => {
      const { value } = e.target
      setCv((prev) => ({ ...prev, [field]: value }))
    }
  }

  function toggleListValue(field, options, value) {
    setCv((prev) => {
      const current = parseList(prev[field])
      const next = current.includes(value)
        ? current.filter((v) => v !== value)
        : [...current, value]
      return { ...prev, [field]: next.join(', ') }
    })
  }

  function updateCustomList(field, options, customValue, setCustomValue) {
    setCustomValue(customValue)
    setCv((prev) => {
      const preset = parseList(prev[field]).filter((v) => options.includes(v))
      return { ...prev, [field]: [...preset, ...parseList(customValue)].join(', ') }
    })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setSaveError('')
    setSaving(true)
    try {
      await saveCv()
      navigate('/preview')
    } catch (err) {
      setSaveError(err.message)
    } finally {
      setSaving(false)
    }
  }

  function handlePhoto(e) {
    const file = e.target.files[0]
    if (!file) return
    const reader = new FileReader()
    reader.onload = () => {
      setCv((prev) => ({ ...prev, photo: reader.result }))
    }
    reader.readAsDataURL(file)
    e.target.value = ''
  }

  function removePhoto() {
    setCv((prev) => ({ ...prev, photo: null }))
  }

  function handleCertificateImages(e) {
    const files = Array.from(e.target.files)
    files.forEach((file) => {
      const reader = new FileReader()
      reader.onload = () => {
        setCv((prev) => ({
          ...prev,
          certificateImages: [
            ...prev.certificateImages,
            { id: `${Date.now()}-${file.name}`, name: file.name, dataUrl: reader.result },
          ],
        }))
      }
      reader.readAsDataURL(file)
    })
    e.target.value = ''
  }

  function removeCertificateImage(id) {
    setCv((prev) => ({ ...prev, certificateImages: prev.certificateImages.filter((img) => img.id !== id) }))
  }

  return (
    <div className="page">
      <h1>Enter Your CV Information</h1>

      <form onSubmit={handleSubmit} className="form form-grid">
        <div className="full photo-field">
          {cv.photo ? (
            <img src={cv.photo} alt="Profile" className="photo-preview" />
          ) : (
            <div className="photo-preview photo-placeholder">No photo</div>
          )}
          <div className="photo-field-controls">
            <label className="photo-upload-label">
              Profile Photo
              <input type="file" accept="image/*" onChange={handlePhoto} />
            </label>
            {cv.photo && (
              <button type="button" className="btn-link" onClick={removePhoto}>
                Remove photo
              </button>
            )}
          </div>
        </div>
        <label>
          Full Name
          <input value={cv.name} onChange={update('name')} required />
        </label>
        <label>
          Job Title / Desired Role
          <input value={cv.jobTitle} onChange={update('jobTitle')} placeholder="Frontend Developer" />
        </label>
        <label>
          Email
          <input type="email" value={cv.email} onChange={update('email')} required />
        </label>
        <label>
          Phone
          <input value={cv.phone} onChange={update('phone')} />
        </label>
        <label>
          Location
          <DropdownSelect
            options={LOCATION_OPTIONS}
            value={cv.location}
            isOther={locationIsOther}
            placeholder="Select a location"
            onSelect={(val) => {
              if (val === 'Other') {
                setLocationIsOther(true)
                setCv((prev) => ({ ...prev, location: '' }))
              } else {
                setLocationIsOther(false)
                setCv((prev) => ({ ...prev, location: val }))
              }
            }}
            onOtherText={update('location')}
          />
        </label>
        <label>
          Languages
          <DropdownMultiSelect
            options={LANGUAGE_OPTIONS}
            selected={parseList(cv.languages)}
            placeholder="Select languages"
            onToggle={(lang) => toggleListValue('languages', LANGUAGE_OPTIONS, lang)}
            customValue={customLanguages}
            onCustomChange={(e) => updateCustomList('languages', LANGUAGE_OPTIONS, e.target.value, setCustomLanguages)}
          />
        </label>
        <label>
          LinkedIn
          <input value={cv.linkedin} onChange={update('linkedin')} placeholder="linkedin.com/in/username" />
        </label>
        <label>
          Portfolio / Website
          <input value={cv.portfolio} onChange={update('portfolio')} placeholder="yourportfolio.com" />
        </label>
        <label className="full">
          Summary
          <textarea rows={3} value={cv.summary} onChange={update('summary')} />
        </label>
        <label className="full">
          Education
          <textarea rows={3} value={cv.education} onChange={update('education')} />
        </label>
        <label className="full">
          Experience
          <textarea rows={4} value={cv.experience} onChange={update('experience')} />
        </label>
        <label className="full">
          Projects
          <textarea
            rows={3}
            value={cv.projects}
            onChange={update('projects')}
            placeholder="Project name – short description of what you built and the tools used"
          />
        </label>
        <label className="full">
          Skills
          <DropdownMultiSelect
            options={SKILL_OPTIONS}
            selected={parseList(cv.skills)}
            placeholder="Select skills"
            onToggle={(skill) => toggleListValue('skills', SKILL_OPTIONS, skill)}
            customValue={customSkills}
            onCustomChange={(e) => updateCustomList('skills', SKILL_OPTIONS, e.target.value, setCustomSkills)}
          />
        </label>
        <label className="full">
          Certificates & Training (one per line)
          <textarea
            rows={3}
            value={cv.certificates}
            onChange={update('certificates')}
            placeholder={'AWS Certified Developer – Associate\nGoogle UX Design Certificate'}
          />
        </label>

        <label className="full">
          Certificate Images
          <input type="file" accept="image/*" multiple onChange={handleCertificateImages} />
        </label>

        {cv.certificateImages.length > 0 && (
          <div className="full cert-image-grid">
            {cv.certificateImages.map((img) => (
              <div key={img.id} className="cert-image-item">
                <img src={img.dataUrl} alt={img.name} />
                <button type="button" className="cert-image-remove" onClick={() => removeCertificateImage(img.id)}>
                  Remove
                </button>
              </div>
            ))}
          </div>
        )}

        {saveError && <p className="error full">{saveError}</p>}

        <div className="full actions">
          <button type="submit" className="btn btn-primary" disabled={saving}>
            {saving ? 'Saving…' : 'Preview CV'}
          </button>
        </div>
      </form>
    </div>
  )
}
