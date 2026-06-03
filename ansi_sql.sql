
CREATE DATABASE event_management;
USE event_management;

-- 1. Users Table
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    city VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL
);

-- 2. Events Table
CREATE TABLE Events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    city VARCHAR(100) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status ENUM('upcoming','completed','cancelled'),
    organizer_id INT,
    FOREIGN KEY (organizer_id) REFERENCES Users(user_id)
);

-- 3. Sessions Table
CREATE TABLE Sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT,
    title VARCHAR(200) NOT NULL,
    speaker_name VARCHAR(100) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

-- 4. Registrations Table
CREATE TABLE Registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    event_id INT,
    registration_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

-- 5. Feedback Table
CREATE TABLE Feedback (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    event_id INT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    feedback_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

-- 6. Resources Table
CREATE TABLE Resources (
    resource_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT,
    resource_type ENUM('pdf','image','link'),
    resource_url VARCHAR(255) NOT NULL,
    uploaded_at DATETIME NOT NULL,
    FOREIGN KEY (event_id) REFERENCES Events(event_id)
);

-- Users Data
INSERT INTO Users(full_name,email,city,registration_date)
VALUES
('Arjun','arjun.reddy@gmail.com','Hyderabad','2024-12-01'),
('Sneha','sneha.sharma@gmail.com','Bengaluru','2024-12-05'),
('Rahul Verma','rahul.verma@gmail.com','Pune','2024-12-10'),
('Priya','priya.nair@gmail.com','Chennai','2025-01-15'),
('Karthik','karthik.rao@gmail.com','Mumbai','2025-02-01');

-- Events Data
INSERT INTO Events(title,description,city,start_date,end_date,status,organizer_id)
VALUES
('Future Tech Summit',
'A networking meetup for software developers and tech innovators.',
'Hyderabad',
'2025-06-10 09:00:00',
'2025-06-10 17:00:00',
'upcoming',
1),

('AI India Conference',
'Conference discussing Artificial Intelligence and Machine Learning trends.',
'Bengaluru',
'2025-05-15 10:00:00',
'2025-05-15 18:00:00',
'completed',
2),

('Full Stack Developer Bootcamp',
'Hands-on workshop covering frontend and backend development.',
'Pune',
'2025-07-01 09:30:00',
'2025-07-03 16:30:00',
'upcoming',
3);

-- Sessions Data
INSERT INTO Sessions(event_id,title,speaker_name,start_time,end_time)
VALUES
(1,'Opening Ceremony','Dr. Anil Kumar',
'2025-06-10 09:00:00',
'2025-06-10 10:00:00'),

(1,'Modern Web Technologies','Sneha Sharma',
'2025-06-10 10:30:00',
'2025-06-10 12:00:00'),

(2,'AI in Healthcare','Rahul Verma',
'2025-05-15 11:00:00',
'2025-05-15 12:30:00'),

(3,'Introduction to React','Karthik Rao',
'2025-07-01 09:30:00',
'2025-07-01 11:30:00');

-- Registrations Data
INSERT INTO Registrations(user_id,event_id,registration_date)
VALUES
(1,1,'2025-05-01'),
(2,1,'2025-05-03'),
(3,2,'2025-04-29'),
(4,2,'2025-04-28'),
(5,3,'2025-06-15');

-- Feedback Data
INSERT INTO Feedback(user_id,event_id,rating,comments,feedback_date)
VALUES
(3,2,5,'Excellent sessions and well organized event.','2025-05-16'),

(4,2,4,'Very useful AI demonstrations and discussions.','2025-05-16'),

(2,1,3,'The event was good but session timings were delayed.','2025-06-11');

-- Resources Data
INSERT INTO Resources(event_id,resource_type,resource_url,uploaded_at)
VALUES
(1,'pdf',
'resources/future_tech_summit_schedule.pdf',
'2025-05-01 10:00:00'),

(2,'image',
'resources/innovate2025_poster.png',
'2025-04-20 09:00:00'),

(3,'link',
'https://developer.mozilla.org/en-US/docs/Web/HTML',
'2025-06-25 15:00:00');

-- EXERCISES

-- 1. User Upcoming Events
SELECT u.full_name, e.title, e.city, e.start_date
FROM Users u
JOIN Registrations r ON u.user_id = r.user_id
JOIN Events e ON r.event_id = e.event_id
WHERE e.status = 'upcoming'
AND u.city = e.city
ORDER BY e.start_date;

-- 2. Top Rated Events
SELECT e.title, AVG(f.rating) AS avg_rating
FROM Events e
JOIN Feedback f ON e.event_id = f.event_id
GROUP BY e.event_id
HAVING COUNT(f.feedback_id) >= 10
ORDER BY avg_rating DESC;

-- 3. Inactive Users
SELECT *
FROM Users
WHERE user_id NOT IN (
    SELECT user_id
    FROM Registrations
    WHERE registration_date >= CURDATE() - INTERVAL 90 DAY
);

-- 4. Peak Session Hours
SELECT e.title, COUNT(s.session_id) AS total_sessions
FROM Events e
JOIN Sessions s ON e.event_id = s.event_id
WHERE TIME(s.start_time) BETWEEN '10:00:00' AND '12:00:00'
GROUP BY e.event_id;

-- 5. Most Active Cities
SELECT u.city, COUNT(DISTINCT r.user_id) AS registrations
FROM Users u
JOIN Registrations r ON u.user_id = r.user_id
GROUP BY u.city
ORDER BY registrations DESC
LIMIT 5;

-- 6. Event Resource Summary
SELECT e.title,
       COUNT(CASE WHEN resource_type='pdf' THEN 1 END) AS pdf_count,
       COUNT(CASE WHEN resource_type='image' THEN 1 END) AS image_count,
       COUNT(CASE WHEN resource_type='link' THEN 1 END) AS link_count
FROM Events e
LEFT JOIN Resources r ON e.event_id = r.event_id
GROUP BY e.event_id;

-- 7. Low Feedback Alerts
SELECT u.full_name, e.title, f.comments, f.rating
FROM Feedback f
JOIN Users u ON f.user_id = u.user_id
JOIN Events e ON f.event_id = e.event_id
WHERE f.rating < 3;

-- 8. Sessions per Upcoming Event
SELECT e.title, COUNT(s.session_id) AS session_count
FROM Events e
LEFT JOIN Sessions s ON e.event_id = s.event_id
WHERE e.status = 'upcoming'
GROUP BY e.event_id;

-- 9. Organizer Event Summary
SELECT u.full_name, e.status, COUNT(e.event_id) AS total_events
FROM Users u
JOIN Events e ON u.user_id = e.organizer_id
GROUP BY u.full_name, e.status;

-- 10. Feedback Gap
SELECT e.title
FROM Events e
JOIN Registrations r ON e.event_id = r.event_id
LEFT JOIN Feedback f ON e.event_id = f.event_id
WHERE f.feedback_id IS NULL
GROUP BY e.event_id;

-- 11. Daily New User Count
SELECT registration_date, COUNT(user_id) AS total_users
FROM Users
WHERE registration_date >= CURDATE() - INTERVAL 7 DAY
GROUP BY registration_date;

-- 12. Event with Maximum Sessions
SELECT e.title, COUNT(s.session_id) AS total_sessions
FROM Events e
JOIN Sessions s ON e.event_id = s.event_id
GROUP BY e.event_id
HAVING total_sessions = (
    SELECT MAX(session_count)
    FROM (
        SELECT COUNT(session_id) AS session_count
        FROM Sessions
        GROUP BY event_id
    ) AS temp
);

-- 13. Average Rating per City
SELECT e.city, AVG(f.rating) AS avg_rating
FROM Events e
JOIN Feedback f ON e.event_id = f.event_id
GROUP BY e.city;

-- 14. Most Registered Events
SELECT e.title, COUNT(r.registration_id) AS total_registrations
FROM Events e
JOIN Registrations r ON e.event_id = r.event_id
GROUP BY e.event_id
ORDER BY total_registrations DESC
LIMIT 3;

-- 15. Event Session Time Conflict
SELECT s1.title, s2.title, s1.event_id
FROM Sessions s1
JOIN Sessions s2
ON s1.event_id = s2.event_id
AND s1.session_id <> s2.session_id
AND s1.start_time < s2.end_time
AND s1.end_time > s2.start_time;

-- 16. Unregistered Active Users
SELECT *
FROM Users
WHERE registration_date >= CURDATE() - INTERVAL 30 DAY
AND user_id NOT IN (
    SELECT user_id
    FROM Registrations
);

-- 17. Multi-Session Speakers
SELECT speaker_name, COUNT(session_id) AS total_sessions
FROM Sessions
GROUP BY speaker_name
HAVING COUNT(session_id) > 1;

-- 18. Resource Availability Check
SELECT e.title
FROM Events e
LEFT JOIN Resources r ON e.event_id = r.event_id
WHERE r.resource_id IS NULL;

-- 19. Completed Events with Feedback Summary
SELECT e.title,
       COUNT(DISTINCT r.registration_id) AS total_registrations,
       AVG(f.rating) AS avg_rating
FROM Events e
LEFT JOIN Registrations r ON e.event_id = r.event_id
LEFT JOIN Feedback f ON e.event_id = f.event_id
WHERE e.status = 'completed'
GROUP BY e.event_id;

-- 20. User Engagement Index
SELECT u.full_name,
       COUNT(DISTINCT r.event_id) AS attended_events,
       COUNT(DISTINCT f.feedback_id) AS feedback_count
FROM Users u
LEFT JOIN Registrations r ON u.user_id = r.user_id
LEFT JOIN Feedback f ON u.user_id = f.user_id
GROUP BY u.user_id;

-- 21. Top Feedback Providers
SELECT u.full_name, COUNT(f.feedback_id) AS total_feedbacks
FROM Users u
JOIN Feedback f ON u.user_id = f.user_id
GROUP BY u.user_id
ORDER BY total_feedbacks DESC
LIMIT 5;

-- 22. Duplicate Registrations Check
SELECT user_id, event_id, COUNT(*) AS duplicate_count
FROM Registrations
GROUP BY user_id, event_id
HAVING COUNT(*) > 1;

-- 23. Registration Trends
SELECT MONTH(registration_date) AS month,
       COUNT(registration_id) AS total_registrations
FROM Registrations
WHERE registration_date >= CURDATE() - INTERVAL 12 MONTH
GROUP BY MONTH(registration_date);

-- 24. Average Session Duration per Event
SELECT e.title,
       AVG(TIMESTAMPDIFF(MINUTE, s.start_time, s.end_time))
       AS avg_duration
FROM Events e
JOIN Sessions s ON e.event_id = s.event_id
GROUP BY e.event_id;

-- 25. Events Without Sessions
SELECT e.title
FROM Events e
LEFT JOIN Sessions s ON e.event_id = s.event_id
WHERE s.session_id IS NULL;

