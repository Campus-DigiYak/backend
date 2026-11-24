package com.digiyak.dispatch.api;

/**
 * Planned REST endpoint receiving runner GPS heartbeats ({@code POST /api/v1/dispatch/runners/location})
 * and writing them to Redis GEO instead of PostgreSQL to avoid high-frequency DB write load.
 */
public class RunnerLocationController {
}
