-- create_tickets_table.sql
-- Creates database MyDBLab (if missing) and a Tickets table suitable for Zendesk-like tickets

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'MyDBLab')
BEGIN
    CREATE DATABASE MyDBLab;
END
GO

USE MyDBLab;
GO

IF OBJECT_ID('dbo.Tickets', 'U') IS NOT NULL
    DROP TABLE dbo.Tickets;
GO

CREATE TABLE dbo.Tickets (
    Id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Subject NVARCHAR(500) NOT NULL,
    Description NVARCHAR(MAX) NULL,
    RequesterName NVARCHAR(250) NULL,
    RequesterEmail NVARCHAR(250) NULL,
    Priority NVARCHAR(50) NULL,
    Status NVARCHAR(50) NULL,
    AssigneeId BIGINT NULL,
    [GroupId] BIGINT NULL,
    Tags NVARCHAR(MAX) NULL,         -- store JSON array (e.g. ["tag1","tag2"]) or comma list
    CustomFields NVARCHAR(MAX) NULL, -- store JSON object/array for arbitrary custom fields
    ZendeskTicketId BIGINT NULL,     -- optional mapping to external ticket id
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    UpdatedAt DATETIME2(3) NULL
);
GO

-- Useful indexes
CREATE INDEX IX_Tickets_RequesterEmail ON dbo.Tickets(RequesterEmail);
CREATE INDEX IX_Tickets_Status ON dbo.Tickets(Status);
CREATE INDEX IX_Tickets_CreatedAt ON dbo.Tickets(CreatedAt);
GO

-- Example: insert a ticket from JSON-like values
-- (adjust values or use application JDBC to insert)
INSERT INTO dbo.Tickets (Subject, Description, RequesterName, RequesterEmail, Priority, Status, Tags, CustomFields)
VALUES (
    'SAMPLE: Venkat',
    'Hello, let''s see how you or your agents can easily respond to and solve tickets.',
    'Test User',
    'test.user@example.com',
    'normal',
    'open',
    N'["zendesk_accelerated_setup"]',
    N'{"assignee_id":24385359002652, "group_id":24385295701532}'
);
GO

-- To remove the example test row uncomment below
-- DELETE FROM dbo.Tickets WHERE Subject = 'SAMPLE: Venkat';
-- GO
