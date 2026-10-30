import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

const JOB_INFO = {
  'Frontend Developer': {
    focus: 'Building the visual, interactive parts of websites and web apps that users see and click on.',
    description:
      'Frontend developers turn designs into working interfaces using HTML, CSS and JavaScript, often with frameworks like React. They care about layout, responsiveness, accessibility and making sure the app feels fast and intuitive for the end user.',
  },
  'Backend Developer': {
    focus: 'Building and maintaining the server-side logic, databases and APIs that power an application behind the scenes.',
    description:
      'Backend developers design the services that process data, enforce business rules and talk to databases, usually using languages like Java, Python or Node.js. They focus on performance, security and making sure the system stays reliable as traffic grows.',
  },
  'Full Stack Developer': {
    focus: 'Working across both the frontend and backend to build a complete, working feature end to end.',
    description:
      'Full stack developers move between the interface a user sees and the server logic behind it, often using a JavaScript-based stack like React and Node.js. They are comfortable with databases, APIs and UI work, which makes them flexible on small teams.',
  },
  'Data Analyst': {
    focus: 'Turning raw data into clear insights that help a business make decisions.',
    description:
      'Data analysts collect, clean and explore data, then use tools like SQL, Excel and Python to spot trends and build reports or dashboards. They work closely with teams to answer specific business questions with evidence rather than guesswork.',
  },
  'Data Scientist': {
    focus: 'Using statistics and machine learning to find patterns in data and build predictive models.',
    description:
      'Data scientists go a step beyond reporting - they build models that forecast outcomes or automate decisions, using tools like Python, pandas and scikit-learn. They need a mix of math, programming and communication skills to turn a model into something a business can actually use.',
  },
  'UI/UX Designer': {
    focus: 'Designing interfaces that are easy, pleasant and intuitive for people to use.',
    description:
      'UI/UX designers research how users behave, sketch wireframes, and build interactive prototypes before a single line of code is written. They care about visual hierarchy, accessibility and consistency, usually working closely with developers to bring designs to life.',
  },
  'DevOps Engineer': {
    focus: 'Automating how software gets built, tested and deployed so releases are fast and reliable.',
    description:
      'DevOps engineers build the pipelines and infrastructure that let a team ship code safely and often, working with containers, cloud platforms and monitoring tools. They bridge development and operations, reducing manual work and downtime.',
  },
  'Project Manager': {
    focus: 'Keeping a project on track by coordinating people, time and budget.',
    description:
      'Project managers plan the work, set milestones, and make sure the team stays aligned with the goal. They handle risks, communicate progress to stakeholders, and remove blockers so the team can deliver on time and within budget.',
  },
  'Product Manager': {
    focus: 'Deciding what a product should do next by balancing user needs, business goals and what is technically possible.',
    description:
      'Product managers gather feedback, prioritize a roadmap, and work with design and engineering to ship features that matter. They spend as much time writing specs and talking to stakeholders as they do looking at usage data.',
  },
  'Marketing Specialist': {
    focus: 'Promoting a product or brand and attracting the right audience through various channels.',
    description:
      'Marketing specialists plan campaigns, write content and track performance across channels like social media, email and search. They use data to understand what resonates with an audience and adjust strategy accordingly.',
  },
}

export default function JobInfo() {
  const navigate = useNavigate()
  const [query, setQuery] = useState('')
  const [expanded, setExpanded] = useState(() => new Set())

  const jobNames = Object.keys(JOB_INFO).filter((j) =>
    j.toLowerCase().includes(query.trim().toLowerCase())
  )

  const toggle = (job) => {
    setExpanded((prev) => {
      const next = new Set(prev)
      if (next.has(job)) {
        next.delete(job)
      } else {
        next.add(job)
      }
      return next
    })
  }

  return (
    <div className="page">
      <h1>Job Information</h1>
      <p className="subtitle">Explore what different jobs focus on before you tailor your CV.</p>

      <label className="job-select">
        Filter jobs
        <input
          type="text"
          placeholder="Search job title..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </label>

      {jobNames.length === 0 ? (
        <div className="skills-result">
          <p className="hint">No job matches your filter. Try a different search.</p>
        </div>
      ) : (
        jobNames.map((job) => {
          const info = JOB_INFO[job]
          const isOpen = expanded.has(job)
          return (
            <div className="skills-result" key={job}>
              <div className="job-info-header">
                <h3>{job}</h3>
                <span
                  className="job-toggle"
                  role="button"
                  tabIndex={0}
                  onClick={() => toggle(job)}
                  onKeyDown={(e) => (e.key === 'Enter' || e.key === ' ') && toggle(job)}
                >
                  {isOpen ? 'Show less' : 'Show more'}
                </span>
              </div>
              {isOpen && (
                <>
                  <p><strong>Focus:</strong> {info.focus}</p>
                  <p>{info.description}</p>
                </>
              )}
            </div>
          )
        })
      )}

      <div className="actions">
        <button className="btn btn-secondary" onClick={() => navigate('/choice')}>
          Back to Menu
        </button>
        <button className="btn btn-primary" onClick={() => navigate('/skills', { state: { from: 'choice' } })}>
          Check Skill Set
        </button>
      </div>
    </div>
  )
}
