export interface IAgent {
  readonly id: number;
  readonly name: string;
  readonly birthDate: Date;
  readonly languages: string[];
  // private AgentRankEnum rank;
  readonly codeName: string;
  readonly passwordHash: string;
  // private DepartmentDTO department;
  readonly missionIds: number[];
  readonly reportIds: number[];
  readonly rank: string;
}
