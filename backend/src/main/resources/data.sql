-- Seed data for jobs, their required skills, and the career-category tree.
-- Wiped and re-inserted on every startup (spring.sql.init.mode=always) so the
-- app always boots with the same demo data as the frontend used to hard-code
-- in JOB_INFO / JOB_SKILLS (src/pages/JobInfo.jsx, src/pages/SkillsCheck.jsx).
-- User accounts and CVs are never touched by this script.

DELETE FROM job_skills;
DELETE FROM jobs;
DELETE FROM job_categories;
DELETE FROM cv_templates;

-- The 3 CV templates (matches TEMPLATES in the old src/templates.js).
INSERT INTO cv_templates (id, name, description, sort_order) VALUES
  ('classic', 'Classic', 'Clean, traditional layout', 1),
  ('modern', 'Modern', 'Bold headers, accent color', 2),
  ('minimal', 'Minimal', 'Whitespace-first, simple', 3);

-- Career category TREE: two roots, each with several leaf categories.
INSERT INTO job_categories (id, name, parent_id) VALUES (1, 'Technology', NULL);
INSERT INTO job_categories (id, name, parent_id) VALUES (2, 'Business', NULL);
INSERT INTO job_categories (id, name, parent_id) VALUES (3, 'Software Development', 1);
INSERT INTO job_categories (id, name, parent_id) VALUES (4, 'Data', 1);
INSERT INTO job_categories (id, name, parent_id) VALUES (5, 'Management', 2);
INSERT INTO job_categories (id, name, parent_id) VALUES (6, 'Design', 1);
INSERT INTO job_categories (id, name, parent_id) VALUES (7, 'Marketing', 2);
INSERT INTO job_categories (id, name, parent_id) VALUES (8, 'Infrastructure', 1);

-- Jobs, each attached to a leaf category.
INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (1, 'Frontend Developer',
   'Building the visual, interactive parts of websites and web apps that users see and click on.',
   'Frontend developers turn designs into working interfaces using HTML, CSS and JavaScript, often with frameworks like React. They care about layout, responsiveness, accessibility and making sure the app feels fast and intuitive for the end user.',
   3);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (2, 'Data Analyst',
   'Turning raw data into clear insights that help a business make decisions.',
   'Data analysts collect, clean and explore data, then use tools like SQL, Excel and Python to spot trends and build reports or dashboards. They work closely with teams to answer specific business questions with evidence rather than guesswork.',
   4);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (3, 'Project Manager',
   'Keeping a project on track by coordinating people, time and budget.',
   'Project managers plan the work, set milestones, and make sure the team stays aligned with the goal. They handle risks, communicate progress to stakeholders, and remove blockers so the team can deliver on time and within budget.',
   5);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (4, 'Backend Developer',
   'Building and maintaining the server-side logic, databases and APIs that power an application behind the scenes.',
   'Backend developers design the services that process data, enforce business rules and talk to databases, usually using languages like Java, Python or Node.js. They focus on performance, security and making sure the system stays reliable as traffic grows.',
   3);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (5, 'Full Stack Developer',
   'Working across both the frontend and backend to build a complete, working feature end to end.',
   'Full stack developers move between the interface a user sees and the server logic behind it, often using a JavaScript-based stack like React and Node.js. They are comfortable with databases, APIs and UI work, which makes them flexible on small teams.',
   3);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (6, 'Data Scientist',
   'Using statistics and machine learning to find patterns in data and build predictive models.',
   'Data scientists go a step beyond reporting - they build models that forecast outcomes or automate decisions, using tools like Python, pandas and scikit-learn. They need a mix of math, programming and communication skills to turn a model into something a business can actually use.',
   4);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (7, 'UI/UX Designer',
   'Designing interfaces that are easy, pleasant and intuitive for people to use.',
   'UI/UX designers research how users behave, sketch wireframes, and build interactive prototypes before a single line of code is written. They care about visual hierarchy, accessibility and consistency, usually working closely with developers to bring designs to life.',
   6);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (8, 'DevOps Engineer',
   'Automating how software gets built, tested and deployed so releases are fast and reliable.',
   'DevOps engineers build the pipelines and infrastructure that let a team ship code safely and often, working with containers, cloud platforms and monitoring tools. They bridge development and operations, reducing manual work and downtime.',
   8);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (9, 'Product Manager',
   'Deciding what a product should do next by balancing user needs, business goals and what is technically possible.',
   'Product managers gather feedback, prioritize a roadmap, and work with design and engineering to ship features that matter. They spend as much time writing specs and talking to stakeholders as they do looking at usage data.',
   5);

INSERT INTO jobs (id, title, focus, description, category_id) VALUES
  (10, 'Marketing Specialist',
   'Promoting a product or brand and attracting the right audience through various channels.',
   'Marketing specialists plan campaigns, write content and track performance across channels like social media, email and search. They use data to understand what resonates with an audience and adjust strategy accordingly.',
   7);

-- Required skills per job (one row per skill - this is the List<String> JPA maps to).
INSERT INTO job_skills (job_id, skill_name) VALUES
  (1, 'HTML'), (1, 'CSS'), (1, 'JavaScript'), (1, 'React'), (1, 'Git');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (2, 'SQL'), (2, 'Excel'), (2, 'Python'), (2, 'Statistics'), (2, 'Data Visualization');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (3, 'Communication'), (3, 'Agile'), (3, 'Risk Management'), (3, 'Budgeting');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (4, 'Java'), (4, 'Spring Boot'), (4, 'SQL'), (4, 'REST APIs'), (4, 'Docker');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (5, 'JavaScript'), (5, 'React'), (5, 'Node.js'), (5, 'SQL'), (5, 'Git');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (6, 'Python'), (6, 'Machine Learning'), (6, 'Statistics'), (6, 'SQL'), (6, 'Data Visualization');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (7, 'Figma'), (7, 'Wireframing'), (7, 'User Research'), (7, 'Prototyping'), (7, 'Typography');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (8, 'Docker'), (8, 'Kubernetes'), (8, 'CI/CD'), (8, 'Linux'), (8, 'AWS');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (9, 'Roadmapping'), (9, 'Stakeholder Management'), (9, 'Agile'), (9, 'Communication'), (9, 'Market Research');

INSERT INTO job_skills (job_id, skill_name) VALUES
  (10, 'SEO'), (10, 'Content Strategy'), (10, 'Social Media'), (10, 'Analytics'), (10, 'Copywriting');
